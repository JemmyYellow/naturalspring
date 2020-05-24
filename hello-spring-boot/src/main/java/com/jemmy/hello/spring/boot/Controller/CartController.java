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
     * @param productId 商品id
     * @param count 商品数量
     * @return
     */
    @RequestMapping(value = "add.do")
    public ServerResponse add(@RequestParam(value = "productid") Integer productId, Integer count) {

        return cartService.add(productId, count);
    }

    public ServerResponse list(){
        return null;
    }
}
