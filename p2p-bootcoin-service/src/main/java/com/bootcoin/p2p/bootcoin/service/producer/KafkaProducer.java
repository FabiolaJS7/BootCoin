package com.bootcoin.p2p.bootcoin.service.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Map<String, ReplyingKafkaTemplate<String, String, String>> replyingKafkaTemplates;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
                         Map<String, ReplyingKafkaTemplate<String, String, String>> replyingKafkaTemplates) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplates = replyingKafkaTemplates;
    }

    public String sendAndReceive(String topic, String replyTopic, String message, String templateKey) {
        try {
            ProducerRecord<String, String> record = createProducerRecord(topic, replyTopic, message);
            record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes());
            log.info("Sending message - Topic: {}, ReplyTopic: {}, Headers: {}", topic, replyTopic, record.headers());

            return replyingKafkaTemplates.get(templateKey).sendAndReceive(record)
                    .toCompletableFuture()
                    .thenApply(ConsumerRecord::value)
                    .get();
        } catch (Exception e) {
            log.error("Error while sending and receiving message for topic {}: {}", topic, e.getMessage(), e);
            throw new RuntimeException("Failed to send and receive message for topic " + topic, e);
        }
    }

    public void sendMessage(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message);
            log.info("Message sent to topic {}: {}", topic, message);
        } catch (Exception e) {
            log.error("Error while sending message to topic {}: {}", topic, e.getMessage(), e);
            throw new RuntimeException("Failed to send message to topic " + topic, e);
        }
    }

    private ProducerRecord<String, String> createProducerRecord(String topic, String replyTopic, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        record.headers().add(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes(StandardCharsets.UTF_8));
        record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
        return record;
    }
}