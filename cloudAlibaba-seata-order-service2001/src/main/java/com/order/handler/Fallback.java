package com.order.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.common.entity.Order;

public class Fallback {
    public static String fallback1(Order order){
        return "fallback1";
    }

    public static String fallback2(){
        return "fallback2";
    }
}
