package com.bootcoin.exchange_bootcoin.api.mapper;

import com.bootcoin.exchange_bootcoin.api.bean.ExchangeRequest;
import com.bootcoin.exchange_bootcoin.api.bean.ExchangeResponse;
import com.bootcoin.exchange_bootcoin.api.model.ExchangeModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeMapper {

    ExchangeMapper INSTANCE = Mappers.getMapper(ExchangeMapper.class);

    ExchangeModel getExchangeModelFromExchangeRequest(ExchangeRequest exchangeRequest);
    ExchangeResponse getExchangeResponseFromExchangeModel(ExchangeModel exchangeModel);
}
