package com.jemmy.hello.spring.boot.common;

public enum TradeStatusEnum {

    TRADE_FINISHED("TRADE_FINISHED", 40),
    TRADE_SUCCESS("TRADE_SUCCESS", 30),
    WAIT_BUYER_PAY("WAIT_BUYER_PAY", 20),
    TRADE_CLOSED("TRADE_CLOSED", 50),

    ;

    private String trade;
    private int status;
    TradeStatusEnum(String trade, int status){
        this.trade = trade;
        this.status = status;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static int statusOf(String trade){
        for(TradeStatusEnum statusEnum:values()){
            if(trade.equals(statusEnum.getTrade())){
                return statusEnum.getStatus();
            }
        }
        return -1;
    }
}
