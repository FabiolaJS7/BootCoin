package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.exchange.ExchangeRequest;
import com.bootcoin.p2p.bootcoin.service.bean.exchange.ExchangeResponse;
import com.bootcoin.p2p.bootcoin.service.producer.KafkaProducer;
import com.bootcoin.p2p.bootcoin.service.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ExchangeServiceImpl implements ExchangeService{

    @Value("${topic.request.exchange}")
    private String requestTopic;
    @Value("${topic.reply.exchange}")
    private String replyTopic;
    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public Mono<ExchangeResponse> getDayExchange(Mono<ExchangeRequest> exchangeRequest) {
        log.info("-> Init exchange day RQ: {}", JsonTransferUtil.objectToJson(exchangeRequest));
        return exchangeRequest
                .flatMap(rq -> {
                    try {
                         String s = kafkaProducer.sendAndReceive(JsonTransferUtil.objectToJson(rq), requestTopic, replyTopic);
                        log.info("Response from exchange: {}", s);
                        return Mono.just(s);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(s -> JsonTransferUtil.jsonToObject(s, ExchangeResponse.class))
                .doOnSuccess(exchangeResponse -> log.info("-> Exchange day RS: {}",
                        JsonTransferUtil.objectToJson(exchangeResponse)));
    }
}
