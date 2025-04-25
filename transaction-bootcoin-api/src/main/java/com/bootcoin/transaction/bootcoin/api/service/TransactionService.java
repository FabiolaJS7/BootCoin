package com.bootcoin.transaction.bootcoin.api.service;

import com.bootcoin.transaction.bootcoin.api.bean.TransactionRequest;
import com.bootcoin.transaction.bootcoin.api.bean.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<TransactionResponse> createTransaction(Mono<TransactionRequest> transactionRequest);
    Flux<TransactionResponse> getTransactions(String walletFrom);
}
