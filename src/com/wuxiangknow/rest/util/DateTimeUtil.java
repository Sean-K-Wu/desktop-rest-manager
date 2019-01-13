package com.wuxiangknow.rest.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * @Desciption 日期比较工具
 * @Author WuXiang
 * @Date 2019/1/9 22:52
 */
public class DateTimeUtil {


    public static boolean compare(Date dateA,Date dateB){
        if(dateA ==null || dateB ==null){
            return false;
        }
        DateTime a = new DateTime(dateA);
        DateTime b = new DateTime(dateB);
        if(a.getMillisOfDay() >= b.getMillisOfDay()){
            return true;
        }
        return false;
    }

    public static boolean isWeekend(Date date) {
        DateTime dateTime = new DateTime(date);
        int week = dateTime.getDayOfWeek();
        if(week >= 6){
            return true;
        }
        return false;
    }
}
