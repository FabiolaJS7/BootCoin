package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionResponse;
import reactor.core.publisher.Flux;

public interface TransactionService {

    Flux<TransactionResponse> getTransactionsByWallet(String walletId);
}
