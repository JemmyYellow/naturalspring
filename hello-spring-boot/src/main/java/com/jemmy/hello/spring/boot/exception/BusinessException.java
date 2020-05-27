package com.jemmy.hello.spring.boot.exception;

public class BusinessException extends RuntimeException{

    private int id;
    private String msg;

    public BusinessException(int id, String msg){
        super(msg);
        this.id = id;
        this.msg = msg;
    }
    public BusinessException(String msg){
        super(msg);
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
