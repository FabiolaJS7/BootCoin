package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionResponse;
import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionUpdateRequest;
import com.bootcoin.p2p.bootcoin.service.producer.KafkaProducer;
import com.bootcoin.p2p.bootcoin.service.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public Flux<TransactionResponse> getTransactionsByWallet(String walletId) {
        log.info("-> Init transactions by walletId: {}", walletId);

        String response = kafkaProducer.sendAndReceiveTransaction(walletId);
        log.info("-> Arrived response: {}", response);

        List<TransactionResponse> transactionResponses = Arrays.asList(JsonTransferUtil.jsonToObject(response,
                TransactionResponse[].class));

        return Flux.fromIterable(transactionResponses);
    }

    @Override
    public Mono<Void> updateTransaction(Mono<TransactionUpdateRequest> transactionUpdateRequest) {
        log.info("-> Init transaction update: {}", JsonTransferUtil.objectToJson(transactionUpdateRequest));
        return transactionUpdateRequest
                .doOnNext(rq -> {
                    kafkaProducer.sendMessage("transaction-update", JsonTransferUtil.objectToJson(rq));
                    log.info("Transaction update sent: {}", JsonTransferUtil.objectToJson(rq));
                })
                .then();
    }
}
