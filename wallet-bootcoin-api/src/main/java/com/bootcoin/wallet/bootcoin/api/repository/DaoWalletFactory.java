package com.bootcoin.wallet.bootcoin.api.repository;

import org.springframework.stereotype.Component;

@Component
public class DaoWalletFactory {

    WalletRepository walletRepository;

    public WalletRepository getWalletRepository() {
        return walletRepository;
    }
}
