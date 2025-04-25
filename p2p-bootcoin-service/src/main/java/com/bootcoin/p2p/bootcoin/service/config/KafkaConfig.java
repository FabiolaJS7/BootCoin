package com.bootcoin.p2p.bootcoin.service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userRequestTopic() {
        return new NewTopic("user-request", 1, (short) 1);
    }

    @Bean
    public NewTopic userResponseTopic() {
        return new NewTopic("user-response", 1, (short) 1);
    }
}
