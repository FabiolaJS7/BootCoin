package com.bootcoin.wallet.bootcoin.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "wallets")
public class WalletModel {

    private String id;
    private String walletAccount;
    private Double amountCoin;
    private String userId;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
