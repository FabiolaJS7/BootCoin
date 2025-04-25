package com.bootcoin.order.bootcoin.api.mapper;

import com.bootcoin.order.bootcoin.api.bean.OrderRequest;
import com.bootcoin.order.bootcoin.api.bean.OrderResponse;
import com.bootcoin.order.bootcoin.api.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderModel getOrderModelFromOrderRequest(OrderRequest orderRequest);
    OrderResponse getOrderResponseFromOrderModel(OrderModel orderModel);
}
