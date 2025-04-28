package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletRequest;
import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletResponse;
import com.bootcoin.p2p.bootcoin.service.producer.KafkaProducer;
import com.bootcoin.p2p.bootcoin.service.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public Mono<WalletResponse> createWallet(Mono<WalletRequest> walletRequest) {
        log.info("-> Init create wallet: {}", JsonTransferUtil.objectToJson(walletRequest));
        return walletRequest
                .flatMap(rq -> {
                    String s = kafkaProducer.sendAndReceiveWallet(JsonTransferUtil.objectToJson(rq));
                    log.info("Response from exchange: {}", s);
                    return Mono.just(s)
                            .map(s1 -> JsonTransferUtil.jsonToObject(s1, WalletResponse.class));
                })
                .doOnNext(walletResponse -> log.info("Wallet created: {}",
                        JsonTransferUtil.objectToJson(walletResponse)));
    }

    @Override
    public Mono<WalletResponse> getWallet(Mono<WalletRequest> walletRequest) {
        return walletRequest
                .doOnNext(rq -> log.info("Init get wallet: {}", JsonTransferUtil.objectToJson(rq)))
                .flatMap(rq -> {
                    String s = kafkaProducer.sendAndReceiveWalletInformation(JsonTransferUtil.objectToJson(rq));
                    log.info("Response from getting wallet: {}", s);
                    return Mono.just(s)
                            .map(s1 -> JsonTransferUtil.jsonToObject(s1, WalletResponse.class));
                })
                .doOnNext(walletResponse -> log.info("Wallet get: {}", walletResponse));
    }
}
