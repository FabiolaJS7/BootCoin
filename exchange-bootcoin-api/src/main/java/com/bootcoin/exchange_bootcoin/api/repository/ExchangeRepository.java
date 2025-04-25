package com.bootcoin.exchange_bootcoin.api.repository;

import com.bootcoin.exchange_bootcoin.api.model.ExchangeModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends ReactiveMongoRepository<ExchangeModel, String> {
}
