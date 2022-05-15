package com.hz.payment.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class BlockHandler {
    public static String handler1(BlockException exception){
        return "handler1";
    }

    public static String handler2(BlockException exception){
        return "handler2";
    }
}
