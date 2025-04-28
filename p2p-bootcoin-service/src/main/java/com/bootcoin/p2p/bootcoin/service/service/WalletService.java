package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.WalletUserResponse;
import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletRequest;
import reactor.core.publisher.Mono;

public interface WalletService {

    Mono<WalletUserResponse> getWallet(Mono<WalletRequest> walletRequest);
}
