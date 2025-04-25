package com.bootcoin.exchange_bootcoin.api.model;

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
@Document(collection = "exchanges")
public class ExchangeModel {

    private String id;
    private LocalDate day;
    private Double priceSell;
    private Double priceBuy;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
