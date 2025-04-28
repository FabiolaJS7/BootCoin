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

    public static final long THIRTY_SECONDS = 30;
    public static final long FORTHY_SECONDS = 30;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ReplyingKafkaTemplate<String, String, String> exchangeReplyingKafkaTemplate(
            ProducerFactory<String, String> producerFactory,
            ConcurrentMessageListenerContainer<String, String> exchangeRepliesContainer) {
        ReplyingKafkaTemplate<String, String, String> template = new ReplyingKafkaTemplate<>(producerFactory, exchangeRepliesContainer);
        template.setDefaultReplyTimeout(Duration.ofSeconds(THIRTY_SECONDS)); // Aumenta el tiempo de espera
        return template;
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> transactionReplyingKafkaTemplate(
            ProducerFactory<String, String> producerFactory,
            ConcurrentMessageListenerContainer<String, String> transactionRepliesContainer) {
        ReplyingKafkaTemplate<String, String, String> template = new ReplyingKafkaTemplate<>(producerFactory, transactionRepliesContainer);
        template.setDefaultReplyTimeout(Duration.ofSeconds(THIRTY_SECONDS));
        return template;
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> bootCoinUserReplyingKafkaTemplate(
            ProducerFactory<String, String> producerFactory,
            ConcurrentMessageListenerContainer<String, String> bootCoinUserRepliesContainer) {
        ReplyingKafkaTemplate<String, String, String> template = new ReplyingKafkaTemplate<>(producerFactory, bootCoinUserRepliesContainer);
        template.setDefaultReplyTimeout(Duration.ofSeconds(THIRTY_SECONDS));
        return template;
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> walletReplyingKafkaTemplate(
            ProducerFactory<String, String> producerFactory,
            ConcurrentMessageListenerContainer<String, String> walletRepliesContainer) {
        ReplyingKafkaTemplate<String, String, String> template = new ReplyingKafkaTemplate<>(producerFactory, walletRepliesContainer);
        template.setDefaultReplyTimeout(Duration.ofSeconds(THIRTY_SECONDS));
        return template;
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> walletUserReplyingKafkaTemplate(
            ProducerFactory<String, String> producerFactory,
            ConcurrentMessageListenerContainer<String, String> walletUserRepliesContainer) {
        ReplyingKafkaTemplate<String, String, String> template = new ReplyingKafkaTemplate<>(producerFactory, walletUserRepliesContainer);
        template.setDefaultReplyTimeout(Duration.ofSeconds(THIRTY_SECONDS));
        return template;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> exchangeRepliesContainer(
            ConsumerFactory<String, String> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties("exchange-response");
        containerProperties.setGroupId("exchange-group");
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> transactionRepliesContainer(
            ConsumerFactory<String, String> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties("transaction-list-response");
        containerProperties.setGroupId("transaction-group");
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> bootCoinUserRepliesContainer(
            ConsumerFactory<String, String> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties("user-response");
        containerProperties.setGroupId("bootuser-group");
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> walletRepliesContainer(
            ConsumerFactory<String, String> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties("wallet-bootcoin-response");
        containerProperties.setGroupId("wallet-group");
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> walletUserRepliesContainer(
            ConsumerFactory<String, String> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties("wallet-user-response");
        containerProperties.setGroupId("wallet-group");
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }

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
}
