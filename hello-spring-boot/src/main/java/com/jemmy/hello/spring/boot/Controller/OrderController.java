package com.jemmy.hello.spring.boot.Controller;

import com.jemmy.hello.spring.boot.service.IOrderService;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/portal/order/")
public class OrderController {

    @Autowired
    IOrderService orderService;
    /**
     * 创建订单
     * @param userId 用户id
     * @return ServerResponse
     */
    @RequestMapping(value = "create.do")
    public ServerResponse create(@RequestParam(value = "userid") Integer userId){
        return orderService.createOrder(userId);
    }

    @RequestMapping(value = "list.do")
    public ServerResponse list(@RequestParam(value = "userid") Integer userId){
        return orderService.getOrderList(userId);
    }
}
