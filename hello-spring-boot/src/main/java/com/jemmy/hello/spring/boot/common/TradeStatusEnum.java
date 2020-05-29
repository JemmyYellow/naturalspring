package com.jemmy.hello.spring.boot.common;

public enum TradeStatusEnum {

    TRADE_FINISHED("交易完成"),
    TRADE_SUCCESS("支付成功"),
    WAIT_BUYER_PAY("交易创建"),
    TRADE_CLOSED("交易关闭"),

    ;

    private String trade;
    TradeStatusEnum(String trade){
        this.trade = trade;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }
}
