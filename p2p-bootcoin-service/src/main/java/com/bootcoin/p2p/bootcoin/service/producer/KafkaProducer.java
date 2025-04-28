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
    private final ReplyingKafkaTemplate<String, String, String> bootCoinUserReplyingKafkaTemplate;
    private final ReplyingKafkaTemplate<String, String, String> walletReplyingKafkaTemplate;
    private final ReplyingKafkaTemplate<String, String, String> walletUserReplyingKafkaTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(ReplyingKafkaTemplate<String, String, String> exchangeReplyingKafkaTemplate,
                         ReplyingKafkaTemplate<String, String, String> transactionReplyingKafkaTemplate,
                         ReplyingKafkaTemplate<String, String, String> bootCoinUserReplyingKafkaTemplate,
                         ReplyingKafkaTemplate<String, String, String> walletReplyingKafkaTemplate,
                         ReplyingKafkaTemplate<String, String, String> walletUserReplyingKafkaTemplate,
                         KafkaTemplate<String, String> kafkaTemplate) {
        this.exchangeReplyingKafkaTemplate = exchangeReplyingKafkaTemplate;
        this.transactionReplyingKafkaTemplate = transactionReplyingKafkaTemplate;
        this.bootCoinUserReplyingKafkaTemplate = bootCoinUserReplyingKafkaTemplate;
        this.walletReplyingKafkaTemplate = walletReplyingKafkaTemplate;
        this.walletUserReplyingKafkaTemplate = walletUserReplyingKafkaTemplate;
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

    public String sendAndReceiveTransaction(String message) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>("transaction-list-request", message);
            record.headers().add(KafkaHeaders.REPLY_TOPIC, "transaction-list-response".getBytes());
            record.headers().add(KafkaHeaders.CORRELATION_ID, new byte[2]);

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
            log.error("Error while sending and receiving message for transaction-list-request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send and receive message for transaction-list-request", e);
        }
    }

    public String sendAndReceiveBootCoinUser(String message) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>("user-create-request", message);
            record.headers().add(KafkaHeaders.REPLY_TOPIC, "user-create-response".getBytes());
            record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes());
            // Log del ProducerRecord
            log.info("Sending bootuser - Topic: {}, Key: {}, Value: {}, Headers: {}",
                    record.topic(),
                    record.key(),
                    record.value(),
                    record.headers());

            return bootCoinUserReplyingKafkaTemplate.sendAndReceive(record)
                    .toCompletableFuture()
                    .thenApply(ConsumerRecord::value)
                    .get();
        } catch (Exception e) {
            log.error("Error while sending and receiving message for bootuser-request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send and receive message for bootuser-request", e);
        }
    }

    public String sendAndReceiveWallet(String message) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>("wallet-bootcoin-request", message);
            record.headers().add(KafkaHeaders.REPLY_TOPIC, "wallet-bootcoin-response".getBytes());
            record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes());

            log.info("CorrelationId wallet: {}", new String(record.headers().lastHeader(KafkaHeaders.CORRELATION_ID).value()));

            return walletReplyingKafkaTemplate.sendAndReceive(record)
                    .toCompletableFuture()
                    .thenApply(ConsumerRecord::value)
                    .get();
        } catch (Exception e) {
            log.error("Error while sending and receiving message for wallet-request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send and receive message for wallet-request", e);
        }
    }

    public String sendAndReceiveWalletInformation(String message) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>("wallet-user-request", message);
            record.headers().add(KafkaHeaders.REPLY_TOPIC, "wallet-user-response".getBytes());
            record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes());

            log.info("CorrelationId wallet user: {}", new String(record.headers().lastHeader(KafkaHeaders.CORRELATION_ID).value()));

            return walletUserReplyingKafkaTemplate.sendAndReceive(record)
                    .toCompletableFuture()
                    .thenApply(ConsumerRecord::value)
                    .get();
        } catch (Exception e) {
            log.error("Error while sending and receiving message for wallet-request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send and receive message for wallet-request", e);
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