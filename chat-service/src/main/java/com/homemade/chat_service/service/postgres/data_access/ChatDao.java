/*
package com.homemade.chat_service.service.postgres.data_access;

import com.homemade.chat_service.model.ChatMessage;
import com.homemade.chat_service.service.postgres.mappers.ChatMapper;
import com.homemade.chat_service.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
public class ChatDao {

    ChatMapper chatMapper;

    public ChatDao(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    public Flux<ChatMessage> getChatMessagesFor(int id, LocalDateTime ts) {
        return Flux.defer(
                () -> Flux.fromIterable(
                        this.chatMapper.findChatMessagesFor(id, ts)
                )).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> insertChatMessage(String txtMessage) {
        String[] splitMessage = txtMessage.split("\\|");
        return Mono.fromCallable(() -> {
            this.chatMapper.insertMessage(
                    splitMessage[0],
                    splitMessage[1],
                    splitMessage[2],
                    DateTimeUtils.stringToLocalDateTime(
                            splitMessage[3], "yyyy-MM-dd HH:mm:ss")
            );
            return null;
        });
    }

}
*/
