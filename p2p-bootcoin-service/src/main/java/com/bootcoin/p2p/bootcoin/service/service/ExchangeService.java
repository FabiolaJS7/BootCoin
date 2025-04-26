package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.exchange.ExchangeRequest;
import com.bootcoin.p2p.bootcoin.service.bean.exchange.ExchangeResponse;
import reactor.core.publisher.Mono;

public interface ExchangeService {

    Mono<ExchangeResponse> getDayExchange(Mono<ExchangeRequest> exchangeRequest);
}
