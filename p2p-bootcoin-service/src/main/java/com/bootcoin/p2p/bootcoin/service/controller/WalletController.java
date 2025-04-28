package com.bootcoin.p2p.bootcoin.service.controller;

import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletRequest;
import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletResponse;
import com.bootcoin.p2p.bootcoin.service.service.WalletService;
import com.bootcoin.p2p.bootcoin.service.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bootcoin/wallet")
@Slf4j
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<WalletResponse>> createWallet(@RequestBody WalletRequest walletRequest) {
        return walletService.createWallet(Mono.just(walletRequest))
                .map(walletResponse -> {
                    log.info("Wallet created successfuly {}", JsonTransferUtil.objectToJson(walletResponse));
                    return ResponseEntity.ok(walletResponse);
                })
                .onErrorResume(e -> {
                    log.error("Error wallet day: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(null));
                });
    }

    @PostMapping(value = ("/information"), consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<WalletResponse>> getWallet(@RequestBody WalletRequest walletRequest) {
        return walletService.getWallet(Mono.just(walletRequest))
                .map(walletResponse -> {
                    log.info("Wallet get successfuly {}", JsonTransferUtil.objectToJson(walletResponse));
                    return ResponseEntity.ok(walletResponse);
                })
                .onErrorResume(throwable -> {
                    log.error("Error get wallet day: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(null));
                });
    }
}
