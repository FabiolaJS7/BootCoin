package com.bootcoin.p2p.bootcoin.service.bean.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    private String status;
    private String transactionNumber;
    private Double amountCoin;
    private Double amountMoney;
    private Double exchangeRate;
    private String walletAccountFrom;
    private String walletAccountTo;
    private String movementType;
    private LocalDate createdAt;
    private LocalDate updatedAt;

}
