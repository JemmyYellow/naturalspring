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
    ALL_CHECK_FAIL(22, "全选执行失败"),
    CART_ID_PRODUCT_NOT_EXISTS(23, "购物车用户id不存在或商品不存在"),
    CHECK_FAIL(24, "更改选中状态失败"),
    CART_ERROR(25, "购物车商品异常"),
    DELETE_CART_ZERO(26, "购物车为空或者清除失败"),
    DELETE_CART_FAIL(26, "购物车商品移除失败"),
    CART_NOT_SELECTED(27, "购物车为空或未选中商品"),
    ORDER_INSERT_FAIL(28, "订单插入失败"),
    ORDERVO_CREATE_FAIL(29, "订单VO创建失败"),
    ORDER_LIST_CREATE_FAIL(30, "订单列表创建失败"),
    ORDER_LIST_SELECT_FAIL(31, "查询订单列表失败"),
    PAY_PARAM_ERROR(32, "支付参数有误"),
    ORDERNO_EMPTY(33, "订单号为空"),
    ORDER_USER_ID_ERROR(34, "订单用户ID不匹配"),
    ORDERNO_SELECT_FAIL(35, "订单号查询失败"),
    ORDER_UPDATE_FAIL(36, "订单更新失败"),
    PAYINFO_SELECT_FAIL(37, "支付信息查询失败"),
    USER_SELECT_FAIL(38, "用户查询失败"),
    ORDER_CLOSE_FAIL(39, "订单未交易成功不可关闭"),
    ORDER_ALREADY_CLOSED(40, "订单已经关闭,请勿重复关闭"),
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
