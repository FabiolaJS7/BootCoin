package com.bootcoin.transaction.bootcoin.api.model;

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
@Document(collection = "transactions")
public class TransactionModel {

    private String id;
    private String transactionNumber;
    private String status;
    private Double amountCoin;
    private Double amountMoney;
    private Double exchangeRate;
    private String walletAccountFrom;
    private String walletAccountTo;
    private String movementType;
    private String paymentForm; // en caso sea ACCEPT
    private String phoneOrAccount; // en caso sea ACCEPT
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
