package com.wuxiangknow.rest.component;

import javax.swing.*;
import java.text.SimpleDateFormat;

/**
 * @Desciption 时间下拉组件
 * @Author WuXiang
 * @Date 2019/1/13 23:38
 */
public class ClockComboBox extends JComboBox {



    public void initItems(int simpleDateFormatField){
        switch (simpleDateFormatField){
            case (SimpleDateFormat.MINUTE_FIELD)://分
                for (int i = 1; i <= 59; i++) {
                    this.addItem(i);
                }
                break;
            case (SimpleDateFormat.HOUR0_FIELD)://时
                for (int i = 0; i <= 23; i++) {
                    this.addItem(i);
                }
                break;
        }
    }

    public void initItems(int simpleDateFormatField,int minValue){
        switch (simpleDateFormatField){
            case (SimpleDateFormat.MINUTE_FIELD)://分
                for (int i = minValue; i <= 59; i++) {
                    this.addItem(i);
                }
                break;
            case (SimpleDateFormat.HOUR0_FIELD)://时
                for (int i = minValue; i <= 23; i++) {
                    this.addItem(i);
                }
                break;
        }
    }
}
