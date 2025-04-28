package com.bootcoin.transaction.bootcoin.api.repository;

import com.bootcoin.transaction.bootcoin.api.model.TransactionModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<TransactionModel, String> {
    Flux<TransactionModel> findTransactionModelByWalletAccountFrom(String walletAccountFrom);

    Mono<TransactionModel> findTransactionModelByTransactionNumber(String transactionNumber);

    Mono<TransactionModel> findTransactionModelById(String transactionId);
}
