package com.homemade.chat_service.service.websocket;

import com.homemade.chat_service.service.kafka.KafkaProducerService;
import com.homemade.chat_service.service.postgres.data_access.MessageDao;
import com.homemade.chat_service.service.zookeeper.ZkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ChatMessagesHandler implements WebSocketHandler {

    ZkService zkService;
    WebSocketSessionManager webSocketSessionManager;
    KafkaProducerService kafkaProducerService;
    MessageDao messageDao;
    AtomicInteger connectionCount;

    public ChatMessagesHandler(
            ZkService zkService,
            KafkaProducerService kafkaProducerService,
            WebSocketSessionManager webSocketSessionManager,
            MessageDao messageDao
    ) {
        this.zkService = zkService;
        this.kafkaProducerService = kafkaProducerService;
        this.webSocketSessionManager = webSocketSessionManager;
        this.messageDao = messageDao;
        this.connectionCount = new AtomicInteger(0);
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        Mono<Principal> principal = session.getHandshakeInfo().getPrincipal();
        return principal.flatMap(p -> {
            String from = p.getName();
            log.info("user {} connected", from);
            this.zkService.registerConnection(this.connectionCount.incrementAndGet());
            this.webSocketSessionManager.addWebSocketSession(from, session);

            //check if this user has any outstanding messages it needs to get
            Mono<Void> send = session.send(
                    this.messageDao
                            .chatMessagesFor(Integer.parseInt(from))
                            .map(e -> session.textMessage(
                                    WebSocketMessageUtility.createPlainTextMessage(
                                            e.getFrom(),
                                            e.getTo(),
                                            e.getTxt(),
                                            e.getSentTs()
                                    )
                            ))
            );

            Mono<Void> receive = session.receive()
                    .map(webSocketMessage -> webSocketMessage.getPayloadAsText())
                    .flatMap(txtMessage -> {
                        String to = WebSocketMessageUtility.getToUserFromChatMessage(txtMessage);

                        //check if receiver online or not
                        Mono<Void> sendToKafka = this.kafkaProducerService
                                .sendMessage(to, txtMessage)
                                .then();

                        Mono<Void> saveToDb = this.messageDao.insertMessage(txtMessage);

                        return Mono.when(sendToKafka, saveToDb);
                    })
                    .then()
                    .doFinally(signalType -> {
                        this.zkService.registerConnection(this.connectionCount.decrementAndGet());
                        this.webSocketSessionManager.removeWebSocketSession(from);
                        log.info("user {} disconnected, signalType {}", from, signalType.toString());
                    });

            return Mono.when(send, receive);
        });
    }
}
