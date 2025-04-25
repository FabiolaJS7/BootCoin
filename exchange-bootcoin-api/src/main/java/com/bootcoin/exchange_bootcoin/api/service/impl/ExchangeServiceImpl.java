package com.bootcoin.exchange_bootcoin.api.service.impl;

import com.bootcoin.exchange_bootcoin.api.bean.ExchangeRequest;
import com.bootcoin.exchange_bootcoin.api.bean.ExchangeResponse;
import com.bootcoin.exchange_bootcoin.api.mapper.ExchangeMapper;
import com.bootcoin.exchange_bootcoin.api.repository.DaoExchangeFactory;
import com.bootcoin.exchange_bootcoin.api.service.ExchangeService;
import com.bootcoin.exchange_bootcoin.api.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {

    DaoExchangeFactory daoExchangeFactory;

    @Override
    public Mono<ExchangeResponse> createExchange(Mono<ExchangeRequest> exchangeRequest) {
        return exchangeRequest
                .doOnNext(rq -> log.info("Init createExchange {}", JsonTransferUtil.objectToJson(rq)))
                .map(ExchangeMapper.INSTANCE::getExchangeModelFromExchangeRequest)
                .flatMap(exchangeModel -> {
                    exchangeModel.setCreatedAt(LocalDate.now());
                    exchangeModel.setUpdatedAt(LocalDate.now());
                    return Mono.just(exchangeModel)
                            .doOnNext(model -> log.info("Exchange creating {}", JsonTransferUtil.objectToJson(model)))
                            .flatMap(model -> daoExchangeFactory.getExchangeRepository().save(model))
                            .map(ExchangeMapper.INSTANCE::getExchangeResponseFromExchangeModel);
                })
                .doOnSuccess(exchangeResponse -> log.info("Exchange created {}", JsonTransferUtil.objectToJson(exchangeResponse)))
                .doOnError(throwable -> log.error("Exchange creation failed {}", throwable.getMessage()));
    }

    @Override
    public Mono<ExchangeResponse> getTodayExchange() {
        log.info("Init get today exchange {}", LocalDate.now());
        return daoExchangeFactory.getExchangeRepository().getExchangeModelByDay(LocalDate.now())
                .map(ExchangeMapper.INSTANCE::getExchangeResponseFromExchangeModel)
                .flatMap(Mono::just)
                .doOnSuccess(exchangeResponse -> log.info("Exchange get today {}",
                        JsonTransferUtil.objectToJson(exchangeResponse)))
                .doOnError(throwable -> log.error("Exchange get today failed {}", throwable.getMessage()));
    }


}
