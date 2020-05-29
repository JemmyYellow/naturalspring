package com.jemmy.hello.spring.boot.common;

public class PayCodeAndDesc {
    /**
     * 订单状态： 20-未付款 30-已付款 40-交易成功 50-交易关闭
     */
    public static final int ORDER_NOT_PAY = 20;
    public static final int ORDER_PAYED = 30;
    public static final int ORDER_SUCCESS = 40;
    public static final int ORDER_CLOSE = 50;

    public static String getPayDesc(int code){
        switch (code){
            case 20:
                return "未付款";
            case 30:
                return "已付款";
            case 40:
                return "交易成功";
            case 50:
                return "交易关闭";
            default:
                return null;
        }
    }
}
