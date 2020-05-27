package com.jemmy.hello.spring.boot.service;

import com.jemmy.hello.spring.boot.utils.ServerResponse;

public interface IOrderService {

    /**
     * 创建订单
     */
    public ServerResponse createOrder(Integer userId);
}
