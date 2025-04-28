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

    @KafkaListener(topics = "wallet-request", groupId = "wallet-group")
    public void createWalletWithResponse(ConsumerRecord<String, String> message,
                             @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic,
                             @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId) {
        log.info("Init request exchange day: {}, replyTopic: {}, correlationId: {}", message.value(),
                replyTopic, correlationId);

        WalletRequest walletRequest = JsonTransferUtil.jsonToObject(message.value(), WalletRequest.class);

        walletService.createWallet(walletRequest.getUserId())
                .flatMap(s -> {
                    WalletResponse walletResponse = new WalletResponse();
                    walletResponse.setUserId(walletRequest.getUserId());
                    walletResponse.setWalletAccount(s);
                    log.info("Wallet created: {}", JsonTransferUtil.objectToJson(walletResponse));

                    ProducerRecord<String, String> responseRecord = new ProducerRecord<>(replyTopic,
                            JsonTransferUtil.objectToJson(walletResponse));
                    responseRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationId);
                    kafkaTemplate.send(responseRecord);
                    return Mono.just("Mensaje enviado al topic " + "wallet-response");
                })
                .doOnNext(s -> log.info("Subscribe to bootcoin wallet"))
                .subscribe();

    }

    @KafkaListener(topics = "wallet-only-create", groupId = "wallet-group")
    public void createWallet(String message) {
        log.info("Wallet to create: {}", message);

        WalletRequest walletRequest = JsonTransferUtil.jsonToObject(message, WalletRequest.class);

        walletService.createWallet(walletRequest.getUserId())
                .flatMap(s -> {
                    WalletResponse walletResponse = new WalletResponse();
                    walletResponse.setUserId(walletRequest.getUserId());
                    walletResponse.setWalletAccount(s);
                    log.info("Wallet created: {}", JsonTransferUtil.objectToJson(walletResponse));
                    return Mono.just("created");
                })
                .doOnNext(s -> log.info("wallet created"))
                .subscribe();

    }
}
