package com.bootcoin.wallet.bootcoin.api.consumer;

import com.bootcoin.wallet.bootcoin.api.bean.WalletRequest;
import com.bootcoin.wallet.bootcoin.api.bean.WalletResponse;
import com.bootcoin.wallet.bootcoin.api.service.WalletService;
import com.bootcoin.wallet.bootcoin.api.util.JsonTransferUtil;
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
public class WalletConsumer {

    @Autowired
    WalletService walletService;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "wallet-bootcoin-request", groupId = "bootcoin-group")
    public void createWalletWithResponse(ConsumerRecord<String, String> message,
                             @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic,
                             @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId) {
        log.info("Init create wallet: {}, replyTopic: {}, correlationId: {}", message.value(),
                replyTopic, correlationId);

        WalletRequest walletRequest = JsonTransferUtil.jsonToObject(message.value(), WalletRequest.class);

        walletService.createWallet(walletRequest.getPhoneNumber())
                .flatMap(s -> {
                    WalletResponse walletResponse = new WalletResponse();
                    walletResponse.setPhoneNumber(walletRequest.getPhoneNumber());
                    walletResponse.setWalletAccount(s);
                    log.info("Wallet created: {}", JsonTransferUtil.objectToJson(walletResponse));

                    ProducerRecord<String, String> responseRecord = new ProducerRecord<>(replyTopic,
                            JsonTransferUtil.objectToJson(walletResponse));
                    responseRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationId);
                    responseRecord.headers().add("Content-Type", "application/json".getBytes());
                    kafkaTemplate.send(responseRecord);
                    return Mono.just("Mensaje enviado al topic " + "wallet-response");
                })
                .doOnNext(s -> log.info("Subscribe to bootcoin wallet"))
                .subscribe();

    }


    @KafkaListener(topics = "wallet-user-request", groupId = "bootcoin-group")
    public void getWallet(ConsumerRecord<String, String> message,
                                         @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic,
                                         @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId) {
        log.info("Init get wallet : {}, replyTopic: {}, correlationId: {}", message.value(),
                replyTopic, correlationId);

        WalletRequest walletRequest = JsonTransferUtil.jsonToObject(message.value(), WalletRequest.class);

        walletService.getWallet(walletRequest.getWalletAccount())
                .flatMap(walletFound -> {
                    log.info("Wallet found: {}", JsonTransferUtil.objectToJson(walletFound));

                    ProducerRecord<String, String> responseRecord = new ProducerRecord<>(replyTopic,
                            JsonTransferUtil.objectToJson(walletFound));
                    responseRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationId);
                    responseRecord.headers().add("Content-Type", "application/json".getBytes());
                    kafkaTemplate.send(responseRecord);
                    return Mono.just("Mensaje enviado al topic " + "wallet-user-response");
                })
                .doOnNext(s -> log.info("Subscribe to bootcoin wallet {}", s))
                .subscribe();

    }
}
