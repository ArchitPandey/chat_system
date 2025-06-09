package com.homemade.chat_service.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaConsumerConfig {

    String bootstrapServers;

    String topicName;

    String autoOffsetReset;

    public KafkaConsumerConfig(
            @Value("${app.kafka.consumer.bootstrap-servers}") String bootStrapServers,
            @Value("${app.kafka.consumer.topic}") String topicName,
            @Value("${app.kafka.consumer.auto-offset-reset}") String autoOffsetReset
    ) {
        this.bootstrapServers = bootStrapServers;
        this.topicName = topicName;
        this.autoOffsetReset = autoOffsetReset;
    }

    @Bean
    ReceiverOptions<String, String> kafkaReactiveConsumerTemplate() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.autoOffsetReset);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "chat-system-consumer-".concat(UUID.randomUUID().toString()));

        ReceiverOptions<String, String> receiverOptions = ReceiverOptions.create(config);
        //receiverOptions = receiverOptions.withValueDeserializer(valueDeserializer());
        receiverOptions = receiverOptions.subscription(Collections.singletonList(this.topicName));

        return receiverOptions;
    }

    /*JsonDeserializer<ChatMessage> valueDeserializer() {
        JsonDeserializer<ChatMessage> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("com.homemade.chat_service.model");
        return jsonDeserializer;
    }*/

}
