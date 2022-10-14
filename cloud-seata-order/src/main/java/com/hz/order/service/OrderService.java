package com.hz.order.service;

import com.common.entity.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderService {
    String/*CommonResult<Order>*/ createOrder(Order order);

    String testCreate(Order order);

    String updateOrder(@Param("userId") Long userId, @Param("status") Integer status);
}
