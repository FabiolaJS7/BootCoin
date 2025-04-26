package com.bootcoin.p2p.bootcoin.service.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate,
                         KafkaTemplate<String, String> kafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendAndReceive(String message, String requestTopic, String replyTopic) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic, null, null, message);
            record.headers().add("kafka_replyTopic", replyTopic.getBytes());

            return replyingKafkaTemplate.sendAndReceive(record)
                    .toCompletableFuture()
                    .thenApply(ConsumerRecord::value)
                    .get(); //espera respuesta del consumer

        } catch (Exception e) {
            log.info("Exception while sending and receiving record: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // Nuevo método para enviar mensajes sin esperar respuesta
    public void sendMessage(String message, String topic) {
        kafkaTemplate.send(topic, message);
    }
}