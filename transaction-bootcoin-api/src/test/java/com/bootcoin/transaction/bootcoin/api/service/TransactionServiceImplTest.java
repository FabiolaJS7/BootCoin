package com.bootcoin.transaction.bootcoin.api.service;

import com.bootcoin.transaction.bootcoin.api.bean.TransactionRequest;
import com.bootcoin.transaction.bootcoin.api.bean.TransactionResponse;
import com.bootcoin.transaction.bootcoin.api.model.TransactionModel;
import com.bootcoin.transaction.bootcoin.api.repository.DaoTransactionFactory;
import com.bootcoin.transaction.bootcoin.api.repository.TransactionRepository;
import org.bouncycastle.util.test.SimpleTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    TransactionServiceImpl transactionService;
    @Mock
    DaoTransactionFactory daoTransactionFactory;
    @Mock
    TransactionRepository transactionRepository;

    @Test
    void createTransaction() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmountCoin(5.0);
        transactionRequest.setAmountMoney(5.0);
        transactionRequest.setExchangeRate(10.0);
        transactionRequest.setStatus("PENDING");

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setAmountCoin(5.0);
        transactionModel.setAmountMoney(50.0);
        transactionModel.setExchangeRate(10.0);
        transactionModel.setStatus("PENDING");

        Mockito.when(daoTransactionFactory.getTransactionRepository()).thenReturn(transactionRepository);
        Mockito.when(transactionRepository.save(any(TransactionModel.class))).thenReturn(Mono.just(transactionModel));

        Mono<TransactionResponse> transactionResponse = transactionService.createTransaction(Mono.just(transactionRequest));

        StepVerifier.create(transactionResponse)
                .expectNextMatches(response -> response.getStatus().equals("PENDING"))
                .verifyComplete();


    }

    @Test
    void getTransactionByWalletFrom_whenSuccessful() {

        TransactionModel transactionModel1 = new TransactionModel();
        transactionModel1.setWalletAccountFrom("WALLET-01");
        transactionModel1.setAmountCoin(5.0);
        transactionModel1.setAmountMoney(50.0);
        transactionModel1.setExchangeRate(10.0);
        transactionModel1.setStatus("PENDING");

        TransactionModel transactionModel2 = new TransactionModel();
        transactionModel1.setWalletAccountFrom("WALLET-01");
        transactionModel2.setAmountCoin(5.0);
        transactionModel2.setAmountMoney(50.0);
        transactionModel2.setExchangeRate(10.0);
        transactionModel2.setStatus("PENDING");

        TransactionModel transactionModel3 = new TransactionModel();
        transactionModel1.setWalletAccountFrom("WALLET-02");
        transactionModel3.setAmountCoin(5.0);
        transactionModel3.setAmountMoney(50.0);
        transactionModel3.setExchangeRate(10.0);
        transactionModel3.setStatus("PENDING");

        Mockito.when(daoTransactionFactory.getTransactionRepository()).thenReturn(transactionRepository);
        Mockito.when(transactionRepository.findTransactionModelByWalletAccountFrom("WALLET-01")).thenReturn(Flux.just(transactionModel1, transactionModel2));

        Flux<TransactionResponse> transactionResponse = transactionService.getTransactions("WALLET-01");

        StepVerifier.create(transactionResponse)
                .expectNextCount(2)
                .verifyComplete();


    }
}