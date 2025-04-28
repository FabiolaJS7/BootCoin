package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.exchange.ExchangeResponse;
import com.bootcoin.p2p.bootcoin.service.bean.user.UserRequest;
import com.bootcoin.p2p.bootcoin.service.bean.user.UserResponse;
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
public class UserServiceImpl implements UserService {

    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public Mono<WalletResponse> createUser(Mono<UserRequest> userRequest) {
        log.info("-> Init create user RQ: {}", JsonTransferUtil.objectToJson(userRequest));
        return userRequest
                .flatMap(rq -> {
                    // crea usuario
                    kafkaProducer.sendMessage("bootuser-request", JsonTransferUtil.objectToJson(rq));
                    log.info("Response from user api: {}", JsonTransferUtil.objectToJson(rq));


                    //create la wallet para el usuario
                    WalletRequest walletRequest = new WalletRequest();
                    walletRequest.setUserId(rq.getPhoneNumber());
                    kafkaProducer.sendMessage("wallet-only-create", JsonTransferUtil.objectToJson(walletRequest));
                    //String s = kafkaProducer.sendAndReceiveWallet(JsonTransferUtil.objectToJson(walletRequest));
                    WalletResponse walletResponse = new WalletResponse();
                    walletResponse.setUserId(rq.getPhoneNumber());
                    return Mono.just(walletResponse);
                })
                .doOnSuccess(walletResponse -> log.info("-> Wallet created RS: {}",
                        JsonTransferUtil.objectToJson(walletResponse)));
    }

    @Override
    public Mono<WalletResponse> createUserAndWallet(Mono<UserRequest> userRequest) {
        log.info("-> Init create user and wallet RQ: {}", JsonTransferUtil.objectToJson(userRequest));
        return userRequest
                .flatMap(rq -> {
                    // crea usuario
                    String responseUser =  kafkaProducer.sendAndReceiveBootCoinUser(JsonTransferUtil.objectToJson(rq));
                    UserResponse userResponse = JsonTransferUtil.jsonToObject(responseUser, UserResponse.class);
                    log.info("Response user api: {}", JsonTransferUtil.objectToJson(userResponse));


                    //create la wallet para el usuario
                    WalletRequest walletRequest = new WalletRequest();
                    walletRequest.setUserId(userResponse.getId());
                    String responseWallet = kafkaProducer.sendAndReceiveWallet(JsonTransferUtil.objectToJson(walletRequest));
                    WalletResponse walletResponse = JsonTransferUtil.jsonToObject(responseWallet, WalletResponse.class);
                    log.info("Response wallet api: {}", JsonTransferUtil.objectToJson(walletResponse));
                    return Mono.just(walletResponse);
                })
                .doOnSuccess(walletResponse -> log.info("-> User and wallet created RS: {}",
                        JsonTransferUtil.objectToJson(walletResponse)));
    }
}
