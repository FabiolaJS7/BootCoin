package com.bootcoin.p2p.bootcoin.service.bean.wallet;

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
    private String userId;
}
