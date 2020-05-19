package com.jemmy.hello.spring.boot.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * 时间格式化工具类
 */
public class DateUtil {

    public static final String STANDARD = "yyyy-MM-dd HH:mm:ss";

    //字符串时间 --> Date类
    public static Date string2Date(String strDate){
        return DateTimeFormat.forPattern(STANDARD).parseDateTime(strDate).toDate();
    }
    public static Date string2Date(String strDate, String format){
        return DateTimeFormat.forPattern(format).parseDateTime(strDate).toDate();
    }


    //Date类 --> 字符串时间
    public static String date2String(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD);
    }

    public static String date2String(Date date, String format){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }
}
