package com.jemmy.hello.spring.boot.Controller;


import com.jemmy.hello.spring.boot.service.IProductService;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/portal/product/")
public class ProductController {

    @Autowired
    IProductService productService;

    @RequestMapping(value = "list.do")
    public ServerResponse list(String keyword,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                               String orderBy){

        ServerResponse serverResponse = productService.list(keyword, pageNum, pageSize, orderBy);
        return serverResponse;
    }
}
