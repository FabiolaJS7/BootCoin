package com.bootcoin.wallet.bootcoin.api.repository;

import com.bootcoin.wallet.bootcoin.api.model.WalletModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends ReactiveMongoRepository<WalletModel, String> {
    Mono<WalletModel> findWalletModelByWalletAccount(String walletAccount);
    Mono<WalletModel> findWalletModelByUserId(String userId);
}
