package com.bootcoin.order.bootcoin.api.service;

import com.bootcoin.order.bootcoin.api.bean.OrderRequest;
import com.bootcoin.order.bootcoin.api.bean.OrderResponse;
import reactor.core.publisher.Mono;

public interface OrderService {

    Mono<OrderResponse> createOrder(Mono<OrderRequest> orderRequest);
}
