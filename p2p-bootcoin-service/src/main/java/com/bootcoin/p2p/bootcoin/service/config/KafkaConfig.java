package com.bootcoin.p2p.bootcoin.service.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private static final long DEFAULT_REPLY_TIMEOUT_SECONDS = 30;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
            ProducerFactory<String, String> producerFactory,
            ConcurrentMessageListenerContainer<String, String> repliesContainer) {
        ReplyingKafkaTemplate<String, String, String> template = new ReplyingKafkaTemplate<>(producerFactory, repliesContainer);
        template.setDefaultReplyTimeout(Duration.ofSeconds(DEFAULT_REPLY_TIMEOUT_SECONDS));
        return template;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer(
            ConsumerFactory<String, String> consumerFactory,
            @Value("${kafka.reply.topics}") String[] replyTopics,
            @Value("${kafka.reply.group-id}") String groupId) {
        ContainerProperties containerProperties = new ContainerProperties(replyTopics);
        containerProperties.setGroupId(groupId);
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Bean
    public Map<String, ReplyingKafkaTemplate<String, String, String>> replyingKafkaTemplates(
            ReplyingKafkaTemplate<String, String, String> exchangeReplyingKafkaTemplate,
            ReplyingKafkaTemplate<String, String, String> transactionReplyingKafkaTemplate,
            ReplyingKafkaTemplate<String, String, String> bootCoinUserReplyingKafkaTemplate,
            ReplyingKafkaTemplate<String, String, String> walletReplyingKafkaTemplate,
            ReplyingKafkaTemplate<String, String, String> userByWalletReplyingKafkaTemplate,
            ReplyingKafkaTemplate<String, String, String> transactionByIdReplyingKafkaTemplate) {
        Map<String, ReplyingKafkaTemplate<String, String, String>> templates = new HashMap<>();
        templates.put("exchangeReplyingKafkaTemplate", exchangeReplyingKafkaTemplate);
        templates.put("transactionReplyingKafkaTemplate", transactionReplyingKafkaTemplate);
        templates.put("bootCoinUserReplyingKafkaTemplate", bootCoinUserReplyingKafkaTemplate);
        templates.put("walletReplyingKafkaTemplate", walletReplyingKafkaTemplate);
        templates.put("userByWalletReplyingKafkaTemplate", userByWalletReplyingKafkaTemplate);
        templates.put("transactionByIdReplyingKafkaTemplate", transactionByIdReplyingKafkaTemplate);
        return templates;
    }
}
