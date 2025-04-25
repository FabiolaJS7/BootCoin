package com.bootcoin.order.bootcoin.api.repository;

import org.springframework.stereotype.Component;

@Component
public class DaoOrderFactory {

    OrderRepository orderRepository;

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }
}
