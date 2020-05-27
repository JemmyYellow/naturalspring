package com.jemmy.hello.spring.boot.Controller;

import com.jemmy.hello.spring.boot.service.ICartService;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/portal/cart/")
public class CartController {

    @Autowired
    ICartService cartService;

    /**
     * 添加商品到购物车
     *
     * @param productId 商品id
     * @param count     商品数量
     * @return ServerResponse
     */
    @RequestMapping(value = "update.do")
    public ServerResponse update(@RequestParam(value = "userid") Integer userId,
                                 @RequestParam(value = "productid") Integer productId,
                                 Integer count) {

        return cartService.update(userId, productId, count);
    }

    /**
     * 购物车列表
     *
     * @return ServerResponse
     */
    @RequestMapping(value = "list.do")
    public ServerResponse list(@RequestParam(value = "userid") Integer userId) {
        return cartService.list(userId);
    }

    /**
     * 变更选中状态
     * @param userId 用户id
     * @param productId 商品id
     * @return ServerResponse
     */
    @RequestMapping(value = "check.do")
    public ServerResponse check(@RequestParam(value = "userid") Integer userId,
                                @RequestParam(value = "productid" ,required = false , defaultValue = "-1") Integer productId){

        return cartService.check(userId, productId);
    }

    /**
     * 移除购物车商品
     * @param userId 用户id
     * @param productId 商品id
     * @return ServerResponse
     */
    @RequestMapping(value = "delete.do")
    public ServerResponse delete(@RequestParam(value = "userid") Integer userId,
                                 @RequestParam(value = "productid" ,required = false , defaultValue = "-1") Integer productId){

        return cartService.delete(userId, productId);
    }
}
