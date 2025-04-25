package com.bootcoin.wallet.bootcoin.api.service;

import com.bootcoin.wallet.bootcoin.api.ActionUpdateConstants;
import com.bootcoin.wallet.bootcoin.api.bean.WalletRequest;
import com.bootcoin.wallet.bootcoin.api.bean.WalletResponse;
import com.bootcoin.wallet.bootcoin.api.bean.WalletUpdateRequest;
import com.bootcoin.wallet.bootcoin.api.model.WalletModel;
import com.bootcoin.wallet.bootcoin.api.repository.DaoWalletFactory;
import com.bootcoin.wallet.bootcoin.api.repository.WalletRepository;
import com.bootcoin.wallet.bootcoin.api.util.NumberRandomUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class WalletServiceImplTest {

    @InjectMocks
    WalletServiceImpl walletService;
    @Spy
    NumberRandomUtil numberRandomUtil;
    @Mock
    DaoWalletFactory daoWalletFactory;
    @Mock
    WalletRepository walletRepository;

    @Test
    void createWallet() {
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setUserId("USER-01");

        WalletModel walletModel = new WalletModel();
        walletModel.setWalletAccount("ACCOUNTID-01");
        walletModel.setAmountCoin(0.00);
        walletModel.setUserId("USER-01");

        Mockito.when(daoWalletFactory.getWalletRepository()).thenReturn(walletRepository);
        Mockito.when(walletRepository.save(any(WalletModel.class))).thenReturn(Mono.just(walletModel));

        Mono<WalletResponse> walletResponse = walletService.createWallet(Mono.just(walletRequest));

        StepVerifier.create(walletResponse)
                .expectNextMatches(response ->
                        walletRequest.getUserId().equals(response.getUserId()) &&
                        response.getAmountCoin() == 0.00 &&
                                !response.getWalletAccount().isEmpty())
                .verifyComplete();

    }

    @Test
    void getWallets() {
        WalletModel walletModel1 = new WalletModel();
        walletModel1.setAmountCoin(10.00);
        walletModel1.setUserId("USER-01");

        WalletModel walletModel2 = new WalletModel();
        walletModel2.setAmountCoin(20.00);
        walletModel2.setUserId("USER-02");

        Flux<WalletModel> walletModelFlux = Flux.just(walletModel1, walletModel2);

        Mockito.when(daoWalletFactory.getWalletRepository()).thenReturn(walletRepository);
        Mockito.when(walletRepository.findAll()).thenReturn(walletModelFlux);

        Flux<WalletResponse> walletResponseFlux = walletService.getWallets();

        StepVerifier.create(walletResponseFlux)
                .expectNextCount(2)
                .verifyComplete();


    }

    @Test
    void updateWallet_whenWalletUpdateWithMoreCoinsBought_thenUpdateWallet() {
        WalletUpdateRequest walletUpdateRequest = new WalletUpdateRequest();
        walletUpdateRequest.setWalletAccount("ACCOUNTID-01");
        walletUpdateRequest.setAmountCoin(20.00);
        walletUpdateRequest.setAction(ActionUpdateConstants.BUY);

        WalletModel walletModelFound = new WalletModel();
        walletModelFound.setWalletAccount("ACCOUNTID-01");
        walletModelFound.setUserId("USER-01");
        walletModelFound.setAmountCoin(0.00);


        Mockito.when(daoWalletFactory.getWalletRepository()).thenReturn(walletRepository);
        Mockito.when(walletRepository.findWalletModelByWalletAccount("ACCOUNTID-01")).thenReturn(Mono.just(walletModelFound));
        Mockito.when(walletRepository.save(any(WalletModel.class))).thenReturn(Mono.just(walletModelFound));

        Mono<WalletResponse> walletResponse = walletService.updateWallet(Mono.just(walletUpdateRequest));
        StepVerifier.create(walletResponse)
                .expectNextMatches(response -> response.getAmountCoin() == 20.00)
                .verifyComplete();

    }

    @Test
    void updateWallet_whenWalletUpdateWithMoreCoinsSold_thenUpdateWallet() {
        WalletUpdateRequest walletUpdateRequest = new WalletUpdateRequest();
        walletUpdateRequest.setWalletAccount("ACCOUNTID-01");
        walletUpdateRequest.setAmountCoin(20.00);
        walletUpdateRequest.setAction(ActionUpdateConstants.SELL);

        WalletModel walletModelFound = new WalletModel();
        walletModelFound.setWalletAccount("ACCOUNTID-01");
        walletModelFound.setUserId("USER-01");
        walletModelFound.setAmountCoin(100.00);


        Mockito.when(daoWalletFactory.getWalletRepository()).thenReturn(walletRepository);
        Mockito.when(walletRepository.findWalletModelByWalletAccount("ACCOUNTID-01")).thenReturn(Mono.just(walletModelFound));
        Mockito.when(walletRepository.save(any(WalletModel.class))).thenReturn(Mono.just(walletModelFound));

        Mono<WalletResponse> walletResponse = walletService.updateWallet(Mono.just(walletUpdateRequest));
        StepVerifier.create(walletResponse)
                .expectNextMatches(response -> response.getAmountCoin() == 80.00)
                .verifyComplete();

    }
}