package com.homemade.chat_service.service.kafka;

import com.homemade.chat_service.service.websocket.WebSocketMessageUtility;
import com.homemade.chat_service.service.websocket.WebSocketSessionManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Objects;

@Service
@Slf4j
public class KafkaRecieverService {

    ReceiverOptions<String, String> receiverOptions;

    WebSocketSessionManager webSocketSessionManager;

    public KafkaRecieverService(
            ReceiverOptions<String, String> receiverOptions,
            WebSocketSessionManager webSocketSessionManager
    ) {
        this.receiverOptions = receiverOptions;
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @PostConstruct
    public void start() {
        KafkaReceiver.create(this.receiverOptions)
                .receive()
                .flatMap(consumerRecord -> {
                    String message = consumerRecord.value();
                    String to = WebSocketMessageUtility.getToUserFromChatMessage(message);
                    WebSocketSession session = this.webSocketSessionManager.getWebSocketSession(to);
                    if (Objects.nonNull(session)) {
                        WebSocketMessage webSocketMessage = session.textMessage(message);
                        return session.send(Mono.just(webSocketMessage));
                    } else {
                        return Mono.empty();
                    }
                })
                .subscribe();
    }

}
