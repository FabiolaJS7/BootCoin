package com.bootcoin.transaction.bootcoin.api.repository;

import org.springframework.stereotype.Component;

@Component
public class DaoTransactionFactory {

    TransactionRepository transactionRepository;

    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }
}
