package com.jemmy.hello.spring.boot.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jemmy.hello.spring.boot.common.ResponseCode;
import com.jemmy.hello.spring.boot.dao.ProductMapper;
import com.jemmy.hello.spring.boot.pojo.Product;
import com.jemmy.hello.spring.boot.service.IProductService;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import com.jemmy.hello.spring.boot.vo.ProductVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class ProductService implements IProductService {

    @Autowired
    ProductMapper productMapper;

    private ProductVO product2vo(Product p) {
        ProductVO productVO = new ProductVO();
        productVO.setId(p.getId());
        productVO.setName(p.getName());
        productVO.setMainImage(p.getMainImage());
        productVO.setPrice(p.getPrice());
        return productVO;
    }

    /**
     * 商品查询
     *
     * @param keyword  关键词
     * @param pageNum  --
     * @param pageSize --
     * @param orderby  后三个参数 github 封装好了 desc asc
     * @return
     */
    @Override
    public ServerResponse list(String keyword, Integer pageNum, Integer pageSize, String orderby) {

        PageHelper.startPage(pageNum, pageSize);
        if (!StringUtils.isBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }
        if (StringUtils.isNotBlank(orderby)) {
            String[] strs = orderby.split("_");
            if (strs.length != 2) {
                return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_ERROR.getCode(),
                        ResponseCode.PARAM_ERROR.getMsg());
            }
            PageHelper.orderBy(strs[0] + " " + strs[1]);
        }
        List<Product> productList = productMapper.list(keyword);
        List<ProductVO> productVOS = new ArrayList<>();
        for (Product p : productList) {
            ProductVO vo = product2vo(p);
            productVOS.add(vo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productVOS);

        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }
}
