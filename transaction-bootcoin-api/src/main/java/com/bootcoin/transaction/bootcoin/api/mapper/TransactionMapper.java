package com.bootcoin.transaction.bootcoin.api.mapper;

import com.bootcoin.transaction.bootcoin.api.bean.TransactionRequest;
import com.bootcoin.transaction.bootcoin.api.bean.TransactionResponse;
import com.bootcoin.transaction.bootcoin.api.model.TransactionModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionModel getTransactionModelFromTransactionRequest(TransactionRequest transactionRequest);
    TransactionResponse getTransactionResponseFromTransactionModel(TransactionModel transactionModel);

}
