package com.bootcoin.p2p.bootcoin.service.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducer {

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;





    public KafkaProducer(ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    public String sendAndReceive(String message, String requestTopic, String replyTopic) throws Exception {
        ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic, null, null, message);
        record.headers().add("kafka_replyTopic", replyTopic.getBytes());

        CompletableFuture<String> future = replyingKafkaTemplate.sendAndReceive(record)
                .toCompletableFuture()
                .thenApply(consumerRecord -> consumerRecord.value());

        return future.get(); // Espera la respuesta del consumidor
    }
}