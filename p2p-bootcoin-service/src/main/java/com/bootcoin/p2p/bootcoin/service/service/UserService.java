package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.user.UserRequest;
import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletResponse;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<WalletResponse> createUser(Mono<UserRequest> userRequest);
    Mono<WalletResponse> createUserAndWallet(Mono<UserRequest> userRequest);
}
