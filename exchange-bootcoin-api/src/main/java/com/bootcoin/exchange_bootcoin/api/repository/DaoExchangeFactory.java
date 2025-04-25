package com.bootcoin.exchange_bootcoin.api.repository;

import org.springframework.stereotype.Component;

@Component
public class DaoExchangeFactory {

    private ExchangeRepository exchangeRepository;

    public ExchangeRepository getExchangeRepository() {
        return exchangeRepository;
    }
}
