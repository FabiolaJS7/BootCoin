package com.bootcoin.p2p.bootcoin.service.controller;

import com.bootcoin.p2p.bootcoin.service.bean.exchange.ExchangeRequest;
import com.bootcoin.p2p.bootcoin.service.bean.exchange.ExchangeResponse;
import com.bootcoin.p2p.bootcoin.service.service.ExchangeService;
import com.bootcoin.p2p.bootcoin.service.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bootcoin/exchange")
@Slf4j
public class ExchangeController {

    @Autowired
    ExchangeService exchangeService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ExchangeResponse>> getDayExchange(@RequestBody ExchangeRequest exchangeRequest) {
        log.info("-> Init create user RQ: {}", JsonTransferUtil.objectToJson(exchangeRequest));
        if (exchangeRequest.getAction().equalsIgnoreCase("GET_EXCHANGE")) {
            return exchangeService.getDayExchange(Mono.just(exchangeRequest))
                    .map(exchangeResponse -> {
                        log.info("Exchange day successfully: {}", JsonTransferUtil.objectToJson(exchangeResponse));
                        return ResponseEntity.ok(exchangeResponse);
                    })
                    .onErrorResume(e -> {
                        log.error("Error exchange day: {}", e.getMessage());
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(null));
                    });
        } else {
            return exchangeService.createExchangeRate(Mono.just(exchangeRequest))
                    .then(Mono.fromCallable(() -> new ResponseEntity<>(HttpStatus.CREATED)));
        }
    }
}
