package com.bootcoin.transaction.bootcoin.api.consumer;

import com.bootcoin.transaction.bootcoin.api.bean.TransactionRequest;
import com.bootcoin.transaction.bootcoin.api.bean.TransactionUpdateRequest;
import com.bootcoin.transaction.bootcoin.api.service.TransactionService;
import com.bootcoin.transaction.bootcoin.api.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TransactionConsumer {

    @Autowired
    TransactionService transactionService;
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "transaction-create", groupId = "bootcoin-group")
    public void createTransaction(String message) {
        log.info("Message to create transaction: {}", message);
        TransactionRequest transactionRequest = JsonTransferUtil.jsonToObject(message, TransactionRequest.class);

        transactionService.createTransaction(Mono.just(transactionRequest))
                .doOnNext(transaction -> log.info("Transaction created: {}",
                        JsonTransferUtil.objectToJson(transaction)))
                .subscribe();
    }

    @KafkaListener(topics = "transaction-list-request", groupId = "bootcoin-group")
    public void getTransactionListByWallet(ConsumerRecord<String, String> message,
                                           @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic,
                                           @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId) {
        log.info("-> Get transaction list request of walletId: {}", message.value());
        transactionService.getTransactions(message.value())
                .collectList().doOnNext(transactions -> {
                    log.info("Transactions found of walletId: {}, {}", message.value(),
                            JsonTransferUtil.objectToJson(transactions));
                    ProducerRecord<String, String> responseRecord = new ProducerRecord<>(replyTopic,
                            JsonTransferUtil.objectToJson(transactions));
                    responseRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationId); // Incluye el correlationId
                    kafkaTemplate.send(responseRecord);
                    log.info("Response sent to topic: {}, Key: {}, Value: {}, Headers: {}",
                            responseRecord.topic(),
                            responseRecord.key(),
                            responseRecord.value(),
                            responseRecord.headers());
                })
                .doOnNext(record -> log.info("Transactions found: {}",
                        JsonTransferUtil.objectToJson(record)))
                .subscribe();

    }

    @KafkaListener(topics = "transaction-update", groupId = "bootcoin-group")
    public void updateTransactionStatus(String message) {
        log.info("Message to update status of transaction: {}", message);

        TransactionUpdateRequest transactionUpdate = JsonTransferUtil.jsonToObject(message,
                TransactionUpdateRequest.class);

        transactionService.updateTransaction(Mono.just(transactionUpdate))
                .doOnNext(transaction -> log.info("Transaction status updated {}",
                        JsonTransferUtil.objectToJson(transaction)))
                .subscribe();
    }

    @KafkaListener(topics = "transaction-by-id-request", groupId = "bootcoin-group")
    public void getTransactionById(ConsumerRecord<String, String> message,
                                           @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic,
                                           @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId) {
        log.info("-> Get transaction list request of walletId: {}", message.value());
        transactionService.getTransactionById(message.value())
                .flatMap(response -> {
                    log.info("Transactions found by id: {}, {}", message.value(),
                            JsonTransferUtil.objectToJson(response));
                    ProducerRecord<String, String> responseRecord = new ProducerRecord<>(replyTopic,
                            JsonTransferUtil.objectToJson(response));
                    responseRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationId); // Incluye el correlationId
                    kafkaTemplate.send(responseRecord);
                    return Mono.just(response);
                })
                .doOnNext(record -> log.info("Transactions found: {}",
                        JsonTransferUtil.objectToJson(record)))
                .subscribe();

    }
}
