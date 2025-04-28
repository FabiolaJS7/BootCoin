package com.bootcoin.wallet.bootcoin.api.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequest {

    private String walletAccount;
    private Double amountCoin;
    private String phoneNumber;
}
