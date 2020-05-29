package com.jemmy.hello.spring.boot.service.impl;

import com.jemmy.hello.spring.boot.common.PayCodeAndDesc;
import com.jemmy.hello.spring.boot.common.ResponseCode;
import com.jemmy.hello.spring.boot.pojo.Order;
import com.jemmy.hello.spring.boot.service.IOrderService;
import com.jemmy.hello.spring.boot.service.IPayService;
import com.jemmy.hello.spring.boot.utils.DateUtil;
import com.jemmy.hello.spring.boot.utils.OrderInfoUtil2_0;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import com.jemmy.hello.spring.boot.vo.OrderVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Service
public class PayService implements IPayService {

    @Value("${alipay.appid}")
    private String APPID;

    @Value("${alipay.rsa2_private}")
    private String RSA2_PRIVATE;

    @Value("${alipay.notify_url}")
    private String NOTIFY_URL;

    @Autowired
    IOrderService orderService;
    /**
     * 支付
     * @param userId 用户id
     * @param orderNo 订单号
     * @return ServerResponse
     */
    @Override
    public ServerResponse pay(Integer userId, Long orderNo) {

        //生成支付信息
        if (StringUtils.isBlank(APPID) || (StringUtils.isBlank(RSA2_PRIVATE))) {
            return ServerResponse.createServerResponseByFail(ResponseCode.PAY_PARAM_ERROR.getCode(),
                    ResponseCode.PAY_PARAM_ERROR.getMsg());
        }
        //根据订单号查询订单
        ServerResponse orderResponse = orderService.findOrderByOrderNo(orderNo);
        if(!orderResponse.isSuccess()){
            return orderResponse;
        }
        List<Order> orderList = (List<Order>)orderResponse.getData();
        if(!orderList.get(0).getUserId().equals(userId)){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_USER_ID_ERROR.getCode(),
                    ResponseCode.ORDER_USER_ID_ERROR.getMsg());
        }
        //生成订单VO
        OrderVO orderVO = orderService.createOrderVO(orderList);
        if(orderVO == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDERVO_CREATE_FAIL.getCode(),
                    ResponseCode.ORDERVO_CREATE_FAIL.getMsg());
        }
        //构造支付订单参数列表
        Map<String, String> params =  OrderInfoUtil2_0.buildOrderParamMap(APPID, true, orderVO, NOTIFY_URL);
        //把参数转成字符串
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        //生成签名
        String sign = OrderInfoUtil2_0.getSign(params, RSA2_PRIVATE, true);
        final String orderInfo = orderParam + "&" + sign;


        return ServerResponse.createServerResponseBySuccess(orderInfo);
    }

    /**
     * 支付回调逻辑
     * @param stringMap 请求参数
     * @return String "success"
     */
    @Override
    public String callbackLogic(Map<String, String> stringMap) {

        final String success = "success";
        //获取订单号
        Long orderNo = Long.valueOf(stringMap.get("out_trade_no"));
        //校验订单号
        ServerResponse orderResponse = orderService.findOrderByOrderNo(orderNo);
        if(!orderResponse.isSuccess()){
            return success;
        }
        //判断订单是否被修改
        List<Order> orderList = (List<Order>) orderResponse.getData();
        Order order = orderList.get(0);
        if(order.getStatus() >= PayCodeAndDesc.ORDER_NOT_PAY){
            //订单已支付
            return success;
        }
        //修改订单状态
        //获取订单的支付状态
        String trade_status = stringMap.get("trade_status");
        Date gmt_payment = DateUtil.string2Date(stringMap.get("gmt_payment")); // 交易付款时间
//        orderService.updateStatus()
        //插入支付信息

        //返回结果

        return null;
    }
}
