package com.bootcoin.p2p.bootcoin.service.bean.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionUpdateRequest {

    private String status;
    private String transactionId;
    private String paymentForm; //YANKI, TRANSFER
    private String phoneOrAccount; // CELULAR or CUENTA
}
