package com.jemmy.hello.spring.boot.service.impl;

import com.jemmy.hello.spring.boot.common.Const;
import com.jemmy.hello.spring.boot.common.PayCodeAndDesc;
import com.jemmy.hello.spring.boot.common.ResponseCode;
import com.jemmy.hello.spring.boot.dao.OrderMapper;
import com.jemmy.hello.spring.boot.exception.BusinessException;
import com.jemmy.hello.spring.boot.pojo.Cart;
import com.jemmy.hello.spring.boot.pojo.Order;
import com.jemmy.hello.spring.boot.pojo.Product;
import com.jemmy.hello.spring.boot.service.ICartService;
import com.jemmy.hello.spring.boot.service.IOrderService;
import com.jemmy.hello.spring.boot.utils.BigDecUtil;
import com.jemmy.hello.spring.boot.utils.DateUtil;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import com.jemmy.hello.spring.boot.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Service
public class OrderService implements IOrderService {

    @Autowired
    ICartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderMapper orderMapper;
    /**
     * 创建订单
     * @param userId 用户id
     * @return ServerResponse
     */
    @Override
    @Transactional(rollbackFor = {BusinessException.class})
    public ServerResponse createOrder(Integer userId) {
        if(userId == null || userId <= 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_EMPTY.getCode(),
                    ResponseCode.PARAMETER_EMPTY.getMsg());
        }
        //在用户购物车中查询已经选中的商品
        ServerResponse serverResponse = cartService.findCartByUserIdAndChecked(userId);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        //将购物车添加到订单信息中
        List<Cart> cartList = (List<Cart>) serverResponse.getData();
        if(cartList == null || cartList.size() == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.CART_ERROR.getCode(),
                    ResponseCode.CART_ERROR.getMsg());
        }
        //生成订单号
        Long orderNo = generateOrderNo(userId);
        //生成订单列表
        ServerResponse createOrderListResponse = createOrderList(cartList, orderNo);
        if(!createOrderListResponse.isSuccess()){
            return createOrderListResponse;
        }
        List<Order> orderList = (List<Order>) createOrderListResponse.getData();

        //计算总价格
//        orderTotalPrice(orderList)
        ////创建orderVO
        Order o0 = orderList.get(0);
        //将时间转化
        Date createTime = DateUtil.string2Date(DateUtil.date2String(o0.getCreateTime()));
        Date updateTime = DateUtil.string2Date(DateUtil.date2String(o0.getUpdateTime()));

        //创建orderVO
        OrderVO orderVO = createOrderVO(orderTotalPrice(orderList), userId, orderNo,
                o0.getStatus(),
                createTime,
                updateTime,
                orderList);
//        if(orderVO == null){
//            return ServerResponse.createServerResponseByFail(ResponseCode.ORDERVO_CREATE_FAIL.getCode(),
//                    ResponseCode.ORDERVO_CREATE_FAIL.getMsg());
//        }
        //清空选中的购物车内容
        deleteCartItem(cartList);

        return ServerResponse.createServerResponseBySuccess(orderVO);
    }
    /**
    生成订单列表，往订单表批量添加订单数据
     */
    private ServerResponse createOrderList(List<Cart> cartList, Long orderNo){
        List<Order> orderList = new ArrayList<>();
        for(Cart cart : cartList){
            Order order = cart2Order(cart, orderNo);
            orderList.add(order);
        }
        if(orderList.size()==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_LIST_CREATE_FAIL.getCode(),
                    ResponseCode.ORDER_LIST_CREATE_FAIL.getMsg());
        }
        //List<Order>插入到订单表
        int insertCount = orderMapper.insertBatch(orderList);
        if(insertCount != orderList.size()){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_INSERT_FAIL.getCode(),
                    ResponseCode.ORDER_INSERT_FAIL.getMsg());
        }
        //查询订单表获得创建和更新时间
        orderList = orderMapper.selectByUserId(orderList.get(0).getUserId());
        if(orderList == null || orderList.size() == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_LIST_SELECT_FAIL.getCode(),
                    ResponseCode.ORDER_LIST_SELECT_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess(orderList);
    }

    private void deleteCartItem(List<Cart> cartList){
        //清空选中的购物车内容
        List<Integer> ids = new ArrayList<>();
        for(Cart cart : cartList){
            ids.add(cart.getId());
        }
        ServerResponse serverResponse1 = cartService.deleteByIds(ids);
        if(!serverResponse1.isSuccess()){
            //删除购物车内容失败
            throw new BusinessException(serverResponse1.getMsg());
        }
    }

    private BigDecimal orderTotalPrice(List<Order> orderList){
        BigDecimal result = new BigDecimal("0");
        for(Order order : orderList){
            result = BigDecUtil.add(String.valueOf(result.doubleValue()), String.valueOf(order.getTotalPrice().doubleValue()));
        }
        return result;
    }

    /**
     * 生成订单VO
     * @param orderTotalPrice 订单总价
     * @param userId 用户id
     * @param orderNo 订单号
     * @param status 订单状态
     * @param createTime 创建时间
     * @param updateTime 更新时间
     * @param orderList 订单列表
     * @return OrderVO
     */
    private OrderVO createOrderVO(BigDecimal orderTotalPrice, Integer userId, Long orderNo,
                                  Integer status, Date createTime, Date updateTime, List<Order> orderList){

        if(orderList == null || orderList.size()==0){
            throw new BusinessException("订单为空");
        }

        //给orderVO赋值
        OrderVO orderVO = new OrderVO();
        orderVO.setUserId(userId);
        orderVO.setOrderNo(orderNo);
        orderVO.setTotalPrice(orderTotalPrice);
        orderVO.setStatus(status);
        orderVO.setCreateTime(createTime);
        orderVO.setUpdateTime(updateTime);
        orderVO.setOrderList(orderList);
        orderVO.setStatusDesc(PayCodeAndDesc.getPayDesc(status));
        return orderVO;
    }

    /**
     * 生成订单(套在循环体内)
     * @param cart 购物车内容
     * @param orderNo 订单号
     * @return Order
     */
    private Order cart2Order (Cart cart, Long orderNo){
        if(cart == null){
            return null;
        }
        Order order = new Order();
        //订单号
        order.setOrderNo(orderNo);
        //商品id
        order.setProductId(cart.getProductId());
        //用户id
        order.setUserId(cart.getUserId());
        /*根据商品id查询商品信息*/
        ServerResponse serverResponse = productService.selectByPrimaryKey(cart.getProductId());
        if(!serverResponse.isSuccess()){
            //商品不存在,抛出异常,在事务中捕捉
            throw new BusinessException(serverResponse.getMsg());
        }
        Product product = (Product) serverResponse.getData();
        //商品名字
        order.setProductName(product.getName());
        //商品主图
        order.setProductImage(product.getMainImage());
        //商品单价
        order.setUnitPrice(product.getPrice());
        //商品数量
        order.setQuantity(cart.getQuantity());
        //商品总价
        BigDecimal totalPrice = BigDecUtil.multiply(String.valueOf(order.getUnitPrice().doubleValue()),
                String.valueOf(order.getQuantity().doubleValue()));
        order.setTotalPrice(totalPrice);
        //订单状态
        order.setStatus(PayCodeAndDesc.ORDER_NOT_PAY);
        return order;
    }

    /**
     * 生成订单号
     * @param userId 用户id
     * @return Long
     */
    public Long generateOrderNo(Integer userId){
        return System.currentTimeMillis()+Math.abs(userId.hashCode());
    }
}
