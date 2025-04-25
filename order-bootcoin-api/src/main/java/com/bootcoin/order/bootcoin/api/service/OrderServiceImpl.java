package com.bootcoin.order.bootcoin.api.service;

import com.bootcoin.order.bootcoin.api.bean.OrderRequest;
import com.bootcoin.order.bootcoin.api.bean.OrderResponse;
import com.bootcoin.order.bootcoin.api.mapper.OrderMapper;
import com.bootcoin.order.bootcoin.api.repository.DaoOrderFactory;
import com.bootcoin.order.bootcoin.api.util.JsonTransferUtil;
import com.bootcoin.order.bootcoin.api.util.NumberRandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    DaoOrderFactory daoOrderFactory;

    @Override
    public Mono<OrderResponse> createOrder(Mono<OrderRequest> orderRequest) {
        return orderRequest
                .doOnNext(rq -> log.info("Init create order {}", JsonTransferUtil.objectToJson(rq)))
                .map(OrderMapper.INSTANCE::getOrderModelFromOrderRequest)
                .flatMap(orderModel -> {
                    return NumberRandomUtil.generateOrderAccount()
                                    .flatMap(s -> {
                                        orderModel.setOrderNumber(s);
                                        orderModel.setCreatedAt(LocalDate.now());
                                        orderModel.setUpdatedAt(LocalDate.now());
                                        return Mono.just(orderModel)
                                                .doOnNext(model -> log.info("Creating order {}", JsonTransferUtil.objectToJson(model)))
                                                .flatMap(model -> daoOrderFactory.getOrderRepository().save(model))
                                                .map(OrderMapper.INSTANCE::getOrderResponseFromOrderModel);
                                    });

                })
                .doOnSuccess(orderResponse -> log.info("Created order {}", JsonTransferUtil.objectToJson(orderResponse)))
                .doOnError(throwable -> log.error("Error creating order {}", throwable.getMessage()));
    }
}
