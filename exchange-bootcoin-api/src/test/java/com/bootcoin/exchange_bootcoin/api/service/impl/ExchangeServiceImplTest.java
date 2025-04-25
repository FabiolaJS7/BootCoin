package com.bootcoin.exchange_bootcoin.api.service.impl;

import com.bootcoin.exchange_bootcoin.api.bean.ExchangeRequest;
import com.bootcoin.exchange_bootcoin.api.bean.ExchangeResponse;
import com.bootcoin.exchange_bootcoin.api.model.ExchangeModel;
import com.bootcoin.exchange_bootcoin.api.repository.DaoExchangeFactory;
import com.bootcoin.exchange_bootcoin.api.repository.ExchangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceImplTest {

    @InjectMocks
    ExchangeServiceImpl exchangeService;

    @Mock
    ExchangeRepository exchangeRepository;

    @Mock
    DaoExchangeFactory daoExchangeFactory;

    @Test
    void createExchange() {
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setDay(LocalDate.now());
        exchangeRequest.setPriceBuy(Double.valueOf(10.00));
        exchangeRequest.setPriceSell(Double.valueOf(12.00));

        ExchangeModel exchangeModel = new ExchangeModel();
        exchangeModel.setId("11111111");
        exchangeModel.setDay(LocalDate.now());
        exchangeModel.setPriceBuy(Double.valueOf(10.00));
        exchangeModel.setPriceSell(Double.valueOf(12.00));

        Mockito.when(daoExchangeFactory.getExchangeRepository()).thenReturn(exchangeRepository);
        Mockito.when(exchangeRepository.save(any(ExchangeModel.class))).thenReturn(Mono.just(exchangeModel));

        Mono<ExchangeResponse> exchangeResponse = exchangeService.createExchange(Mono.just(exchangeRequest));

        StepVerifier.create(exchangeResponse)
                .expectNextMatches(response -> response.getPriceBuy().equals(exchangeRequest.getPriceBuy()))
                .verifyComplete();
    }
}