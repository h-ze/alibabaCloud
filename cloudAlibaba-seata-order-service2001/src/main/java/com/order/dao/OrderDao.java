package com.order.dao;

import com.common.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {
    int createOrder(Order order);

    int updateOrder(@Param("userId") Long userId,@Param("status") Integer status);
}
