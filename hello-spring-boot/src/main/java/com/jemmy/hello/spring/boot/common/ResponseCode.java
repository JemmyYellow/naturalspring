package com.jemmy.hello.spring.boot.common;

public enum ResponseCode {

    USERNAME_EMPTY(1, "用户名不能为空"),

    PASSWORD_EMPTY(2, "密码不能为空"),

    USERNAME_NOT_EXISTS(3, "用户名不存在"),

    PASSWORD_ERROR(4, "密码错误"),

    PARAMETER_EMPTY(5, "参数不能为空"),

    PHONE_EMPTY(6, "手机号不能为空"),

    USERNAME_EXISTS(7, "用户名存在"),

    PHONE_EXISTS(8, "手机存在"),

    REGISTER_FAIL(9, "注册失败"),

    NEED_LOGIN(10, "需要登录"),

    PASSWORD_IS_SAME(11, "新密码与原密码相同"),

    CHANGE_PASSWORD_FAIL(12, "修改密码失败"),

    LOGIN_FAIL(13, "登陆失败"),

    PARAM_ERROR(14, "参数错误"),

    SIGN_OUT_FAIL(15, "注销失败"),

    CART_UPDATE_FAIL(16, "购物车更改失败"),

    PRODUCT_NOT_EXISTS(17, "商品不存在"),

    OLD_PASSWORD_WRONG(18, "原密码不对"),

    USERID_NOT_EXISTS(19, "用户ID不存在"),

    PHONE_IS_SAME(20, "请输入新的手机号"),

    PHONE_CHANGE_FAIL(21, "手机更改失败"),

    ;

    private int code;
    private String msg;

    ResponseCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
