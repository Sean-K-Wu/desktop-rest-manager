package com.wuxiangknow.rest.bean;

import com.wuxiangknow.rest.util.DateTimeUtil;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * @Desciption 时间区间
 * @Author WuXiang
 * @Date 2019/1/14 11:50
 */
public class BetweenTime implements Serializable{
    private static final long serialVersionUID = -7343021146959703844L;

    private DateTime startTime;//开始时间
    private DateTime endTime;//结束时间




    public BetweenTime() {
        startTime = new DateTime(1992,4,28,0,0);
        endTime = new DateTime(1992,4,28,23,59);
    }

    public BetweenTime(int startHour ,int startMinute ,int endHour ,int endMinute) {
        startTime = new DateTime(1992,4,28,startHour,startMinute);
        endTime = new DateTime(1992,4,28,endHour,endMinute);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getStartHour(){
        if(this.startTime!=null){
            return this.startTime.getHourOfDay();
        }
        return null;
    }

    public Integer getStartMinute(){
        if(this.startTime!=null){
            return this.startTime.getMinuteOfHour();
        }
        return null;
    }
    public Integer getEndHour(){
        if(this.endTime!=null){
            return this.endTime.getHourOfDay();
        }
        return null;
    }

    public Integer getEndMinute(){
        if(this.endTime!=null){
            return this.endTime.getMinuteOfHour();
        }
        return null;
    }

    public boolean isBetween(DateTime now) {
        if(startTime ==null){
            if(endTime != null){
                if(DateTimeUtil.compare(now,endTime)){
                    return false;
                }
            }
        }else{
            if(!DateTimeUtil.compare(now,startTime)){
                return false;
            }else{
                if(endTime != null){
                    if(DateTimeUtil.compare(now,endTime)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
