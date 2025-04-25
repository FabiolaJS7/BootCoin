package com.bootcoin.wallet.bootcoin.api.service;

import com.bootcoin.wallet.bootcoin.api.bean.WalletRequest;
import com.bootcoin.wallet.bootcoin.api.bean.WalletResponse;
import com.bootcoin.wallet.bootcoin.api.bean.WalletUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletService {

    Mono<WalletResponse> createWallet(Mono<WalletRequest> walletRequest);
    Flux<WalletResponse> getWallets();
    Mono<WalletResponse> updateWallet(Mono<WalletUpdateRequest> walletUpdateRequest);
}
