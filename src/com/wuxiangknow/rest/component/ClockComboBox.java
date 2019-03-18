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
        clearAllItem();
        switch (simpleDateFormatField){
            case (SimpleDateFormat.MINUTE_FIELD)://分
                for (int i = 0; i <= 59; i++) {
                    if(i<10){
                        this.addItem(String.valueOf(0).concat(String.valueOf(i)));
                    }else{
                        this.addItem(String.valueOf(i));
                    }
                }
                break;
            case (SimpleDateFormat.HOUR0_FIELD)://时
                for (int i = 0; i <= 23; i++) {
                    if(i<10){
                        this.addItem(String.valueOf(0).concat(String.valueOf(i)));
                    }else{
                        this.addItem(String.valueOf(i));
                    }
                }
                break;
        }
    }

    public void initItems(int simpleDateFormatField,int minValue){
        clearAllItem();
        switch (simpleDateFormatField){
            case (SimpleDateFormat.MINUTE_FIELD)://分
                for (int i = minValue; i <= 59; i++) {
                    if(i<10){
                        this.addItem(String.valueOf(0).concat(String.valueOf(i)));
                    }else{
                        this.addItem(String.valueOf(i));
                    }
                }
                break;
            case (SimpleDateFormat.HOUR0_FIELD)://时
                for (int i = minValue; i <= 23; i++) {
                    if(i<10){
                        this.addItem(String.valueOf(0).concat(String.valueOf(i)));
                    }else{
                        this.addItem(String.valueOf(i));
                    }
                }
                break;
        }
    }

    public void clearAllItem(){
        this.removeAllItems();
        this.addItem("  ");
    }


    public String getClockItem(Integer i){
        if(i != null){
            String item = String.valueOf(i);
            if(i < 10){
                item = "0".concat(item);
            }
            return item;
        }
        return "  ";

    }

}
