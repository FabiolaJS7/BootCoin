package com.bootcoin.p2p.bootcoin.service.bean.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequest {

    private LocalDate day;
    private String action;
    private Double priceSell;
    private Double priceBuy;
}
