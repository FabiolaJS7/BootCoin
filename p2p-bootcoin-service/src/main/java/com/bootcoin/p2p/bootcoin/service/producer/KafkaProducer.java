package com.bootcoin.p2p.bootcoin.service.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class KafkaProducer {

    private final ReplyingKafkaTemplate<String, String, String> exchangeReplyingKafkaTemplate;
    private final ReplyingKafkaTemplate<String, String, String> transactionReplyingKafkaTemplate;
    private final ReplyingKafkaTemplate<String, String, String> userReplyingKafkaTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(ReplyingKafkaTemplate<String, String, String> exchangeReplyingKafkaTemplate,
                         ReplyingKafkaTemplate<String, String, String> transactionReplyingKafkaTemplate,
                         ReplyingKafkaTemplate<String, String, String> userReplyingKafkaTemplate,
                         KafkaTemplate<String, String> kafkaTemplate) {
        this.exchangeReplyingKafkaTemplate = exchangeReplyingKafkaTemplate;
        this.transactionReplyingKafkaTemplate = transactionReplyingKafkaTemplate;
        this.userReplyingKafkaTemplate = userReplyingKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendAndReceiveExchange(String message) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>("exchange-request", message);
            record.headers().add(KafkaHeaders.REPLY_TOPIC, "exchange-response".getBytes());
            record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes());

            log.info("Sending exchange - Topic: {}, Key: {}, Value: {}, Headers: {}",
                    record.topic(),
                    record.key(),
                    record.value(),
                    record.headers());

            return exchangeReplyingKafkaTemplate.sendAndReceive(record)
                    .toCompletableFuture()
                    .thenApply(ConsumerRecord::value)
                    .get();
        } catch (Exception e) {
            log.error("Error while sending and receiving message for exchange-request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send and receive message for exchange-request", e);
        }
    }

    public String sendAndReceiveUser(String message) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>("user-request", message);
            record.headers().add(KafkaHeaders.REPLY_TOPIC, "user-response".getBytes());
            record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes());

            // Log del ProducerRecord
            log.info("Sending user - Topic: {}, Key: {}, Value: {}, Headers: {}",
                    record.topic(),
                    record.key(),
                    record.value(),
                    record.headers());

            return userReplyingKafkaTemplate.sendAndReceive(record)
                    .toCompletableFuture()
                    .thenApply(ConsumerRecord::value)
                    .get();
        } catch (Exception e) {
            log.error("Error while sending and receiving message for user-request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send and receive message for user-request", e);
        }
    }

    public String sendAndReceiveTransaction(String message) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>("transaction-list-request", message);
            record.headers().add(KafkaHeaders.REPLY_TOPIC, "transaction-list-response".getBytes());
            record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes());

            // Log del ProducerRecord
            log.info("Sending transaction list - Topic: {}, Key: {}, Value: {}, Headers: {}",
                    record.topic(),
                    record.key(),
                    record.value(),
                    record.headers());

            return transactionReplyingKafkaTemplate.sendAndReceive(record)
                    .toCompletableFuture()
                    .thenApply(ConsumerRecord::value)
                    .get();
        } catch (Exception e) {
            log.error("Error while sending and receiving message for user-request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send and receive message for user-request", e);
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
}