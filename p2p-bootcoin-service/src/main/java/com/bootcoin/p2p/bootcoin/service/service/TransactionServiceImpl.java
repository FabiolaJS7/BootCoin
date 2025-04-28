package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionRequest;
import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionResponse;
import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionUpdateRequest;
import com.bootcoin.p2p.bootcoin.service.bean.user.UserResponse;
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

        String response = kafkaProducer.sendAndReceive(
                "transaction-list-request",
                "transaction-list-response",
                walletId,
                "transactionReplyingKafkaTemplate"
        );
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

                    String transactionById = kafkaProducer.sendAndReceive(
                            "transaction-by-id-request",
                            "transaction-by-id-response",
                            rq.getTransactionId(),
                            "transactionByIdReplyingKafkaTemplate"
                    );
                    TransactionResponse transactionResponse = JsonTransferUtil.jsonToObject(transactionById,
                            TransactionResponse.class);
                    log.info("transactionById {}", JsonTransferUtil.objectToJson(transactionResponse));

                    String userByWalletAccount = kafkaProducer.sendAndReceive(
                            "user-by-wallet-request",
                            "user-by-wallet-response",
                            transactionResponse.getWalletAccountTo(),
                            "userByWalletReplyingKafkaTemplate"
                    );
                    UserResponse userResponseByWallet = JsonTransferUtil.jsonToObject(userByWalletAccount,
                            UserResponse.class);
                    log.info("userResponseByWallet {}", JsonTransferUtil.objectToJson(userResponseByWallet));

                    kafkaProducer.sendMessage("transaction-update", JsonTransferUtil.objectToJson(rq));
                    log.info("Transaction update sent: {}", JsonTransferUtil.objectToJson(rq));
                })
                .then();
    }

    @Override
    public Mono<Void> createTransaction(Mono<TransactionRequest> transactionRequest, String walletFrom) {
        log.info("-> Init create transaction {}", JsonTransferUtil.objectToJson(transactionRequest));
        return transactionRequest
                .doOnNext(rq -> {
                    rq.setWalletAccountFrom(walletFrom);
                    kafkaProducer.sendMessage("transaction-create", JsonTransferUtil.objectToJson(rq));
                    log.info("Transaction create sent: {}", JsonTransferUtil.objectToJson(rq));
                })
                .then();
    }
}
