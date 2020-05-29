package com.jemmy.hello.spring.boot.service;

import com.jemmy.hello.spring.boot.utils.ServerResponse;

import java.util.Map;

public interface IPayService {
    public ServerResponse pay(Integer userId, Long orderNo);

    public String callbackLogic(Map<String, String> stringMap);
}
