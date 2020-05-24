package com.jemmy.hello.spring.boot.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVO {
    private List<CartProductVO> data;
    private Boolean allChecked;
    private BigDecimal cartTotalPrice;

    public List<CartProductVO> getData() {
        return data;
    }

    public void setData(List<CartProductVO> data) {
        this.data = data;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
