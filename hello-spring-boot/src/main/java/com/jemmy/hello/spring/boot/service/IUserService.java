package com.jemmy.hello.spring.boot.service;

import com.jemmy.hello.spring.boot.pojo.User;
import com.jemmy.hello.spring.boot.utils.ServerResponse;

public interface IUserService {

    /**
     * 登录
     */
    public ServerResponse loginLogic(String username, String password);

    /**
     * 注册
     */
    public ServerResponse registerLogic(User user);

    /**
     * 修改用户信息
     */
    public ServerResponse updateUserLogic(User user);

    /**
     * 修改密码
     */
    public ServerResponse changepwdLogic(Integer id, String old, String now);

    /**
     * 注销
     */
    public ServerResponse signOut(String username);

    /**
     * 修改手机号
     */
    public ServerResponse changePhone(Integer userId, String newphone);
}
