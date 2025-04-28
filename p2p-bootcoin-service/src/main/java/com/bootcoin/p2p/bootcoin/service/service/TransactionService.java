package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionRequest;
import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionResponse;
import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Flux<TransactionResponse> getTransactionsByWallet(String walletId);
    Mono<Void> updateTransaction(Mono<TransactionUpdateRequest> transactionUpdateRequest);
    Mono<Void> createTransaction(Mono<TransactionRequest> transactionRequest, String walletFrom);
}
