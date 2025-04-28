package com.bootcoin.wallet.bootcoin.api.service;

import com.bootcoin.wallet.bootcoin.api.constants.ActionUpdateConstants;
import com.bootcoin.wallet.bootcoin.api.bean.WalletResponse;
import com.bootcoin.wallet.bootcoin.api.bean.WalletUpdateRequest;
import com.bootcoin.wallet.bootcoin.api.mapper.WalletMapper;
import com.bootcoin.wallet.bootcoin.api.model.WalletModel;
import com.bootcoin.wallet.bootcoin.api.repository.DaoWalletFactory;
import com.bootcoin.wallet.bootcoin.api.util.JsonTransferUtil;
import com.bootcoin.wallet.bootcoin.api.util.NumberRandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    @Autowired
    DaoWalletFactory daoWalletFactory;

    @Override
    public Mono<String> createWallet(String userId) {
        log.info("Init create wallet to user {}", userId);

        return daoWalletFactory.getWalletRepository().findWalletModelByUserId(userId)
                .map(WalletModel::getWalletAccount)
                .doOnNext(s -> log.info("UserId have wallet yet"))
                .switchIfEmpty(
                        NumberRandomUtil.generateWalletAccount()
                                .flatMap(s -> {
                                    WalletModel walletModel = new WalletModel();
                                    walletModel.setAmountCoin(0.00);
                                    walletModel.setWalletAccount(s);
                                    walletModel.setCreatedAt(LocalDate.now());
                                    walletModel.setUpdatedAt(LocalDate.now());
                                    walletModel.setUserId(userId);

                                    log.info("Creating new wallet for user {}: {}", userId,
                                            JsonTransferUtil.objectToJson(walletModel));
                                    return daoWalletFactory.getWalletRepository()
                                            .save(walletModel)
                                            .map(WalletModel::getWalletAccount);
                                })
                )
                .doOnSuccess(walletAccount -> log.info("Wallet created successfully {} for userId {}",
                        walletAccount, userId))
                .doOnError(throwable -> log.error("Wallet created error {}", throwable.getMessage()));
    }

    @Override
    public Flux<WalletResponse> getWallets() {
        log.info("Init get wallets");
        return daoWalletFactory.getWalletRepository().findAll()
                .map(WalletMapper.INSTANCE::getWalletResponseFromWalletModel)
                .doOnNext(walletResponse -> log.info("Wallets get {}", JsonTransferUtil.objectToJson(walletResponse)))
                .switchIfEmpty(Flux.just(new WalletResponse()))
                .doOnError(throwable -> log.error("Wallets get error {}", throwable.getMessage()));
    }

    @Override
    public Mono<WalletResponse> updateWallet(Mono<WalletUpdateRequest> walletUpdateRequest) {
        return walletUpdateRequest
                .doOnNext(rq -> log.info("Init update wallet {}", JsonTransferUtil.objectToJson(rq)))
                .flatMap(rq -> daoWalletFactory.getWalletRepository().findWalletModelByWalletAccount(rq.getWalletAccount())
                        .flatMap(walletModel -> {
                                walletModel.setAmountCoin(rq.getAction().equalsIgnoreCase(ActionUpdateConstants.SELL)
                                        ? walletModel.getAmountCoin() - rq.getAmountCoin()
                                        : walletModel.getAmountCoin() + rq.getAmountCoin());
                                return daoWalletFactory.getWalletRepository().save(walletModel);

                        })).map(WalletMapper.INSTANCE::getWalletResponseFromWalletModel)
                .doOnSuccess(walletResponse -> log.info("Wallet updated {}", JsonTransferUtil.objectToJson(walletResponse)))
                .switchIfEmpty(Mono.just(new WalletResponse()))
                .doOnError(throwable -> log.error("Wallet updated error {}", throwable.getMessage()));
    }
}
