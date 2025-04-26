package com.bootcoin.p2p.bootcoin.service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.reply}")
    private String replyTopic;

    public KafkaConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${topic.reply.exchange}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        log.info("Consume message: {}", message);
        System.out.println("Message received: " + message);

        // Procesa el mensaje
        String response = "Processed: " + message;

        // Envía la respuesta al topic de salida
        kafkaTemplate.send(replyTopic, response);
        log.info("Response sent: {}", response);
    }
}
