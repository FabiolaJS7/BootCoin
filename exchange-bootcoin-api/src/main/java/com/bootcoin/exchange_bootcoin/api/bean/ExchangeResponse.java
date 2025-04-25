package com.bootcoin.exchange_bootcoin.api.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeResponse {

    private String id;
    private LocalDate day;
    private Double priceSell;
    private Double priceBuy;
}
