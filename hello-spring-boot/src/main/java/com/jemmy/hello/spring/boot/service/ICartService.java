package com.jemmy.hello.spring.boot.service;

import com.jemmy.hello.spring.boot.utils.ServerResponse;

public interface ICartService {
    public ServerResponse add(Integer productId, Integer count);
}
