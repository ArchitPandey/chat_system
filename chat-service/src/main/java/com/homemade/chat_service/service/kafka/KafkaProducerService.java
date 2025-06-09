package com.homemade.chat_service.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class KafkaProducerService {

    ReactiveKafkaProducerTemplate<String, String> producerTemplate;

    String topicName;

    public KafkaProducerService(
            @Qualifier("messageSenderTemplate") ReactiveKafkaProducerTemplate<String, String> producerTemplate,
            @Value("${app.kafka.producer.topic}") String topicName
    ) {
        this.producerTemplate = producerTemplate;
        this.topicName = topicName;
    }

    public Mono<RecordMetadata> sendMessage(String to, String message) {
        return this.producerTemplate
                .send(this.topicName, to, message)
                .map(e -> e.recordMetadata())
                .doOnNext(e -> {
                    log.info(
                            "sent to {}:{}:{}",
                            e.topic(),
                            e.partition(),
                            e.offset()
                    );
                })
                .doFinally(signalType -> {
                    log.info("completed {}", signalType.toString());
                });
    }

}
