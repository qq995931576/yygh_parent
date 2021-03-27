package com.achang.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期操作工具类
 *
 * @author qy
 * @since 1.0
 */
public class DateUtil {

    private static final String dateFormat = "yyyy-MM-dd";

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);

    }

    /**
     * 在日期date上增加amount天 。
     *
     * @param date   处理的日期，非null
     * @param amount 要加的天数，可能为负数
     */
    public static Date addDays(Date date, int amount) {
        Calendar now =Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+amount);
        return now.getTime();
    }

    //演示测试
    public static void main(String[] args) {
        System.out.println(DateUtil.formatDate(new Date()));//今天
        System.out.println(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));//前一天
    }

    /**
     * 根据日期获取周几数据
     * @param dateTime
     * @return
     */
    public static String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }
}
