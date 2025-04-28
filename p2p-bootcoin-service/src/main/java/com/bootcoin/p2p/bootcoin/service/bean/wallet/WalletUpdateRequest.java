package com.bootcoin.p2p.bootcoin.service.bean.wallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletUpdateRequest {

    private String walletAccount;
    private String action;
    private Double amountCoin;
}
