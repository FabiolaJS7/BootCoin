package com.bootcoin.p2p.bootcoin.service.service;

import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletRequest;
import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletResponse;
import reactor.core.publisher.Mono;

public interface WalletService {

    Mono<WalletResponse> createWallet(Mono<WalletRequest> walletRequest);
}
