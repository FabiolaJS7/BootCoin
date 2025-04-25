package com.bootcoin.order.bootcoin.api.service;

import com.bootcoin.order.bootcoin.api.bean.OrderRequest;
import com.bootcoin.order.bootcoin.api.bean.OrderResponse;
import com.bootcoin.order.bootcoin.api.model.OrderModel;
import com.bootcoin.order.bootcoin.api.repository.DaoOrderFactory;
import com.bootcoin.order.bootcoin.api.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.MockitoHint;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    OrderServiceImpl orderService;
    @Mock
    DaoOrderFactory daoOrderFactory;
    @Mock
    OrderRepository orderRepository;

    @Test
    void createOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAmountCoin(500.00);
        orderRequest.setAmountPayedMoney(50.00);
        orderRequest.setExchangeRate(10.00);
        orderRequest.setOrderNumber("123456");

        OrderModel orderModel = new OrderModel();
        orderModel.setAmountCoin(500.00);
        orderModel.setAmountPayedMoney(50.00);
        orderModel.setExchangeRate(10.00);
        orderModel.setOrderNumber("123456");

        Mockito.when(daoOrderFactory.getOrderRepository()).thenReturn(orderRepository);
        Mockito.when(orderRepository.save(any(OrderModel.class))).thenReturn(Mono.just(orderModel));

        Mono<OrderResponse> responseMono = orderService.createOrder(Mono.just(orderRequest));

        StepVerifier.create(responseMono)
                .expectNextMatches(orderResponse ->
                        orderResponse.getOrderNumber().equals(orderRequest.getOrderNumber()))
                .expectComplete()
                .verify();



    }
}