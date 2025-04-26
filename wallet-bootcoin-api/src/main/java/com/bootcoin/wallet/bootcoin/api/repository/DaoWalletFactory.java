package com.bootcoin.wallet.bootcoin.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DaoWalletFactory {

    @Autowired
    WalletRepository walletRepository;

    public WalletRepository getWalletRepository() {
        return walletRepository;
    }
}
