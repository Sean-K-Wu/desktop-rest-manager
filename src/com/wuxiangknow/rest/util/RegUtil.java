package com.wuxiangknow.rest.util;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/1/12 17:16
 */
public class RegUtil {

    public static boolean isIntegerNumber(String value){
        if(value!=null){
            return value.matches("^[+]?[\\d]*$");
        }
        return false;
    }
}
