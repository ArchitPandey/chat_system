package com.homemade.chat_service.service.postgres.data_access;

import com.homemade.chat_service.model.ChatMessage;
import com.homemade.chat_service.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@Slf4j
public class MessageDao {

    private final DatabaseClient db;

    public MessageDao(DatabaseClient db) {
        this.db = db;
    }

    public Flux<ChatMessage> chatMessagesFor(int userId) {
        return db.sql("select from_id, to_id, txt, sent_ts from chat_system.messages where to_id = :userId")
                .bind("userId", userId)
                .map(row -> new ChatMessage(
                        row.get("from_id", String.class),
                        row.get("to_id", String.class),
                        row.get("txt", String.class),
                        row.get("sent_ts", LocalDateTime.class)
                ))
                .all()
                .doOnError(e -> {
                    log.error("error selecting unsent messages for userId {}", userId);
                    log.error("error {}", e);
                });
    }

    public Mono<Void> insertMessage(String txtMessage) {
        String[] splitMessage = txtMessage.split("\\|");
        return db.sql("insert into chat_system.messages (from_id, to_id, txt, sent_ts) values (:from_id, :to_id, :txt, :sent_ts)")
                .bind("from_id", Integer.parseInt(splitMessage[0]))
                .bind("to_id", Integer.parseInt(splitMessage[1]))
                .bind("txt", splitMessage[2])
                .bind("sent_ts", DateTimeUtils
                        .stringToLocalDateTime(
                                splitMessage[3],
                                "yyyy-MM-dd HH:mm:ss"
                        )
                )
                .then()
                .doOnError(e -> {
                    log.error("error inserting record");
                    log.error("error {}", e);
                });
    }

}
