package com.bootcoin.exchange_bootcoin.api.service;

import com.bootcoin.exchange_bootcoin.api.bean.ExchangeRequest;
import com.bootcoin.exchange_bootcoin.api.bean.ExchangeResponse;
import reactor.core.publisher.Mono;

public interface ExchangeService {

    Mono<ExchangeResponse> createExchange(Mono<ExchangeRequest> exchangeRequest);
}
