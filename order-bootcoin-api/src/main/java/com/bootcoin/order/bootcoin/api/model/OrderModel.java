package com.bootcoin.order.bootcoin.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class OrderModel {

    private String id;
    private String orderNumber;
    private Double amountCoin;
    private Double amountPayedMoney;
    private Double exchangeRate;
    private String walletAccountFrom;
    private String walletAccountTo;
    private String movementType;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
