package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.user.UserRequest;
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
    public Mono<WalletResponse> createUserAndWallet(Mono<UserRequest> userRequest) {

        return userRequest
                .flatMap(rq -> {
                    log.info("-> Init create user and wallet RQ: {}", JsonTransferUtil.objectToJson(rq));
                    //create la wallet para el usuario
                    WalletRequest walletRequest = new WalletRequest();
                    walletRequest.setPhoneNumber(rq.getPhoneNumber());
                    String responseWallet = kafkaProducer.sendAndReceive(
                            "wallet-bootcoin-request",
                            "wallet-bootcoin-response",
                            JsonTransferUtil.objectToJson(walletRequest),
                            "walletReplyingKafkaTemplate"
                    );

                    WalletResponse walletResponse = JsonTransferUtil.jsonToObject(responseWallet, WalletResponse.class);
                    log.info("Response wallet api: {}", JsonTransferUtil.objectToJson(walletResponse));

                    // crea usuario
                    rq.setWalletAccount(walletResponse.getWalletAccount());
                    String responseUser = kafkaProducer.sendAndReceive(
                            "user-create-request",
                            "user-create-response",
                            JsonTransferUtil.objectToJson(rq),
                            "bootCoinUserReplyingKafkaTemplate"
                    );
                    log.info("Response user api: {}", JsonTransferUtil.objectToJson(responseUser));

                    return Mono.just(walletResponse);
                })
                .doOnSuccess(walletResponse -> log.info("-> User and wallet created RS: {}",
                        JsonTransferUtil.objectToJson(walletResponse)));
    }
}
