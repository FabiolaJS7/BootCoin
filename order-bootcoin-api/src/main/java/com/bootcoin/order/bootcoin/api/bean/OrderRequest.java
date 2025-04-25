package com.bootcoin.order.bootcoin.api.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private String orderNumber;
    private Double amountCoin;
    private Double amountPayedMoney;
    private Double exchangeRate;
    private String walletAccountFrom;
    private String walletAccountTo;
    private String movementType;

}
