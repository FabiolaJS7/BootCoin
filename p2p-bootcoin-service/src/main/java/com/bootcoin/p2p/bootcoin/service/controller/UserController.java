package com.bootcoin.p2p.bootcoin.service.controller;

import com.bootcoin.p2p.bootcoin.service.bean.user.UserRequest;
import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletResponse;
import com.bootcoin.p2p.bootcoin.service.service.UserService;
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
@RequestMapping("/bootcoin/users")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<WalletResponse>> createUserWallet(@RequestBody UserRequest userRequest) {
        log.info("-> Init create user and wallet RQ: {}", JsonTransferUtil.objectToJson(userRequest));
        return userService.createUserAndWallet(Mono.just(userRequest))
                .map(walletResponse -> {
                    log.info("User create and walletsuccessfully: {}", JsonTransferUtil.objectToJson(walletResponse));
                    return ResponseEntity.ok(walletResponse);
                })
                .onErrorResume(e -> {
                    log.error("Error creating user and wallet: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new WalletResponse()));
                });
    }

}
