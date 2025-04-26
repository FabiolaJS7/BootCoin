package com.bootcoin.transaction.bootcoin.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DaoTransactionFactory {

    @Autowired
    TransactionRepository transactionRepository;

    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }
}
