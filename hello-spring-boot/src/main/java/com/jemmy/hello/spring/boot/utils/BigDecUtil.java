package com.jemmy.hello.spring.boot.utils;

import java.math.BigDecimal;

public class BigDecUtil {
    public static BigDecimal add(String num1, String num2){
        BigDecimal result;
        BigDecimal temp1 = new BigDecimal(num1);
        BigDecimal temp2 = new BigDecimal(num2);
        result = temp1.add(temp2);
        return result;
    }
    public static BigDecimal multiply(String num1, String num2){
        BigDecimal result;
        BigDecimal temp1 = new BigDecimal(num1);
        BigDecimal temp2 = new BigDecimal(num2);
        result = temp1.multiply(temp2);
        return result;
    }
}
