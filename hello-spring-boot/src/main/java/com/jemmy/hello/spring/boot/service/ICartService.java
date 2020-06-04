package com.jemmy.hello.spring.boot.service;

import com.jemmy.hello.spring.boot.utils.ServerResponse;

import java.util.List;

public interface ICartService {
    public ServerResponse update(Integer userId, Integer productId, Integer count);

    public ServerResponse add(Integer userId, Integer productId);

    public ServerResponse list(Integer userId);

    public ServerResponse check(Integer userId, Integer productId);

    public ServerResponse delete(Integer userId, Integer productId);

    public ServerResponse findCartByUserIdAndChecked(Integer userId);

    public ServerResponse deleteByIds(List<Integer> ids);
}
