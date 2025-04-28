package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.exchange.ExchangeRequest;
import com.bootcoin.p2p.bootcoin.service.bean.exchange.ExchangeResponse;
import com.bootcoin.p2p.bootcoin.service.producer.KafkaProducer;
import com.bootcoin.p2p.bootcoin.service.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public Mono<ExchangeResponse> getDayExchange(Mono<ExchangeRequest> exchangeRequest) {
        log.info("-> Init exchange day RQ: {}", JsonTransferUtil.objectToJson(exchangeRequest));
        return exchangeRequest
                .flatMap(rq -> {
                    String s = kafkaProducer.sendAndReceive(
                            "exchange-request",
                            "exchange-response",
                            JsonTransferUtil.objectToJson(rq),
                            "exchangeReplyingKafkaTemplate"
                    );
                    log.info("Response from exchange: {}", s);
                    return Mono.just(s);
                })
                .map(s -> JsonTransferUtil.jsonToObject(s, ExchangeResponse.class))
                .doOnSuccess(exchangeResponse -> log.info("-> Exchange day RS: {}",
                        JsonTransferUtil.objectToJson(exchangeResponse)));
    }

    @Override
    public Mono<Void> createExchangeRate(Mono<ExchangeRequest> exchangeRequest) {
        log.info("-> Init create exchange rate RQ: {}", JsonTransferUtil.objectToJson(exchangeRequest));
        return exchangeRequest
                .flatMap(rq -> {
                    kafkaProducer.sendMessage("exchange-rate", JsonTransferUtil.objectToJson(rq));
                    log.info("Response from createExchangeRate: {}", JsonTransferUtil.objectToJson(rq));
                    return Mono.just("Exchange rate created");
                })
                .doOnSuccess(s -> log.info(s))
                .then();
    }
}
