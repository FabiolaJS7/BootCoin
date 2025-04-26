package com.bootcoin.exchange_bootcoin.api.consumer;

import com.bootcoin.exchange_bootcoin.api.bean.ExchangeRequest;
import com.bootcoin.exchange_bootcoin.api.service.ExchangeService;

import com.bootcoin.exchange_bootcoin.api.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class ExchangeConsumer {

    private static final String EXCHANGE_RESPONSE = "exchange-response";

    @Autowired
    ExchangeService exchangeService;
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "exchange-rate", groupId = "exchange-group")
    public void createExchange(String message) {
        log.info("Exchange recibido: {}", message);
        ExchangeRequest exchangeRequest= JsonTransferUtil.jsonToObject(message, ExchangeRequest.class);

        exchangeService.createExchange(Mono.just(exchangeRequest))
                .doOnSuccess(exchangeResponse -> log.info("Exchange created: {}",
                        JsonTransferUtil.objectToJson(exchangeResponse)))
                .subscribe();

    }

    @KafkaListener(topics = "exchange-request", groupId = "exchange-group")
    public void getExchange(ConsumerRecord<String, String> message,
                        @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic,
                        @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId){
        log.info("Init request exchange day: {}, replyTopic: {}, correlationId: {}", message.value(),
                replyTopic, correlationId);
        exchangeService.getTodayExchange()
                .doOnNext(exchangeResponse -> log.info("Exchange day: {}",
                        JsonTransferUtil.objectToJson(exchangeResponse)))
                .flatMap(exchangeResponse -> {
                    log.info("Sending as response {}", JsonTransferUtil.objectToJson(exchangeResponse));
                    ProducerRecord<String, String> responseRecord = new ProducerRecord<>(replyTopic,
                            JsonTransferUtil.objectToJson(exchangeResponse));
                    responseRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationId); // Incluye el correlationId
                    kafkaTemplate.send(responseRecord);
                    return Mono.just("Mensaje enviado al topic " + EXCHANGE_RESPONSE);
                })
                .doOnSuccess(s -> log.info("Successfully: {}", s))
                .subscribe();

    }
}
