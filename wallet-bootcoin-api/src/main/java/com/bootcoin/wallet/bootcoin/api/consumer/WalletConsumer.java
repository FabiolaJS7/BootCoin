package com.bootcoin.wallet.bootcoin.api.consumer;

import com.bootcoin.wallet.bootcoin.api.bean.WalletRequest;
import com.bootcoin.wallet.bootcoin.api.service.WalletService;
import com.bootcoin.wallet.bootcoin.api.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WalletConsumer {

    @Autowired
    WalletService walletService;

    @KafkaListener(topics = "bootcoin-wallet-create", groupId = "user_group")
    public void createWallet(String message) {
        log.info("-> Init create wallet: {}", message);
        WalletRequest walletRequest = JsonTransferUtil.jsonToObject(message, WalletRequest.class);

        walletService.createWallet(walletRequest.getUserId())
                .doOnSubscribe(s -> log.info("Subscribe to bootcoin wallet"))
                .subscribe();
    }

}
