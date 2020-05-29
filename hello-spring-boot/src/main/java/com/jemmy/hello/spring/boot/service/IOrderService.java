package com.jemmy.hello.spring.boot.service;

import com.jemmy.hello.spring.boot.pojo.Order;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import com.jemmy.hello.spring.boot.vo.OrderVO;

import java.util.Date;
import java.util.List;

public interface IOrderService {

    /**
     * 创建订单
     */
    public ServerResponse createOrder(Integer userId);

    /**
     * 根据订单号查询订单
     */
    public ServerResponse findOrderByOrderNo(Long orderNo);

    /**
     * 根据订单列表生成订单VO
     */
    public OrderVO createOrderVO(List<Order> orderList);

    /**
     * 查询订单
     */
    public ServerResponse getOrderList(Integer userId);

    /**
     * 更新订单的支付状态
     */
    public ServerResponse updateStatus(Long orderNo, Integer status);
}
