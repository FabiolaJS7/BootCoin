package com.bootcoin.transaction.bootcoin.api.service;

import com.bootcoin.transaction.bootcoin.api.bean.TransactionRequest;
import com.bootcoin.transaction.bootcoin.api.bean.TransactionResponse;
import com.bootcoin.transaction.bootcoin.api.bean.TransactionUpdateRequest;
import com.bootcoin.transaction.bootcoin.api.mapper.TransactionMapper;
import com.bootcoin.transaction.bootcoin.api.repository.DaoTransactionFactory;
import com.bootcoin.transaction.bootcoin.api.util.JsonTransferUtil;
import com.bootcoin.transaction.bootcoin.api.util.NumberRandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    DaoTransactionFactory daoTransactionFactory;

    @Override
    public Mono<TransactionResponse> createTransaction(Mono<TransactionRequest> transactionRequest) {
        return transactionRequest
                .doOnNext(rq -> log.info("Init create transaction {}", JsonTransferUtil.objectToJson(rq)))
                .map(TransactionMapper.INSTANCE::getTransactionModelFromTransactionRequest)
                .flatMap(transactionModel -> {
                    transactionModel.setStatus("PENDING");
                    transactionModel.setCreatedAt(LocalDate.now());
                    transactionModel.setUpdatedAt(LocalDate.now());
                    return Mono.just(transactionModel)
                            .doOnNext(model -> log.info("Creating transaction {}", JsonTransferUtil.objectToJson(model)))
                            .flatMap(model -> daoTransactionFactory.getTransactionRepository().save(model));
                })
                .map(TransactionMapper.INSTANCE::getTransactionResponseFromTransactionModel)
                .doOnSuccess(transactionResponse -> log.info("Transaction created {}", JsonTransferUtil.objectToJson(transactionResponse)))
                .doOnError(throwable -> log.error("Error transaction create {}", throwable.getMessage()));
    }

    @Override
    public Flux<TransactionResponse> getTransactions(String walletFrom) {
        log.info("Get transactions from wallet {}", walletFrom);
        return daoTransactionFactory.getTransactionRepository().findTransactionModelByWalletAccountFrom(walletFrom)
                .map(TransactionMapper.INSTANCE::getTransactionResponseFromTransactionModel)
                .doOnNext(transactionResponse -> log.info("Transactions get {}", JsonTransferUtil.objectToJson(transactionResponse)))
                .switchIfEmpty(Flux.just(new TransactionResponse()))
                .doOnError(throwable -> log.error("Error transactions get {}", throwable.getMessage()));

    }

    @Override
    public Mono<TransactionResponse> updateTransaction(Mono<TransactionUpdateRequest> transactionUpdateRequest) {
        return transactionUpdateRequest
                .doOnNext(rq -> log.info("Init update transaction {}, {}", rq.getTransactionNumber(),
                        JsonTransferUtil.objectToJson(rq)))
                .flatMap(rq -> daoTransactionFactory.getTransactionRepository().findTransactionModelByTransactionNumber(rq.getTransactionNumber())
                        .flatMap(transactionModelFound -> {
                           return NumberRandomUtil.generateOrderAccount()
                                   .flatMap(s -> {
                                       transactionModelFound.setStatus(rq.getStatus());
                                       transactionModelFound.setUpdatedAt(LocalDate.now());
                                       transactionModelFound.setTransactionNumber(rq.getStatus().equalsIgnoreCase("ACCEPTED") ? s : null);
                                       return Mono.just(transactionModelFound)
                                               .flatMap(model -> daoTransactionFactory.getTransactionRepository().save(model))
                                               .map(TransactionMapper.INSTANCE::getTransactionResponseFromTransactionModel);
                                   });
                        }))
                .switchIfEmpty(Mono.just(new TransactionResponse()))
                .doOnSuccess(transactionResponse -> log.info("Transactions updated {}",
                        JsonTransferUtil.objectToJson(transactionResponse)))
                .doOnError(throwable -> log.error("Error transactions updated {}", throwable.getMessage()));
    }
}
