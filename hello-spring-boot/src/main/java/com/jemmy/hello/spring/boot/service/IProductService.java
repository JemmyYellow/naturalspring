package com.jemmy.hello.spring.boot.service;

import com.jemmy.hello.spring.boot.utils.ServerResponse;

public interface IProductService {

    public ServerResponse list(String keyword,
                               Integer pageNum,
                               Integer pageSize,
                               String orderBy);

}
