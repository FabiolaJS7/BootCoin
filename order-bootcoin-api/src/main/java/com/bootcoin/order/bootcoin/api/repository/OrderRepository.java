package com.bootcoin.order.bootcoin.api.repository;

import com.bootcoin.order.bootcoin.api.model.OrderModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository  extends ReactiveCrudRepository<OrderModel, Long> {
}
