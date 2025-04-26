package com.bootcoin.transaction.bootcoin.api.consumer;

import com.bootcoin.transaction.bootcoin.api.bean.TransactionRequest;
import com.bootcoin.transaction.bootcoin.api.service.TransactionService;
import com.bootcoin.transaction.bootcoin.api.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TransactionConsumer {

    @Autowired
    TransactionService transactionService;

    @KafkaListener(topics = "transaction-request", groupId = "transaction-group")
    public void createTransaction(String message) {
        log.info("Message to create transaction: {}", message);
        TransactionRequest transactionRequest= JsonTransferUtil.jsonToObject(message, TransactionRequest.class);

        transactionService.createTransaction(Mono.just(transactionRequest))
                .doOnNext(transaction -> log.info("Transaction created: {}",
                        JsonTransferUtil.objectToJson(transaction)))
                .subscribe();
    }
}
