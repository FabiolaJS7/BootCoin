package com.bootcoin.exchange_bootcoin.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DaoExchangeFactory {

    @Autowired
    private ExchangeRepository exchangeRepository;

    public ExchangeRepository getExchangeRepository() {
        return exchangeRepository;
    }
}
