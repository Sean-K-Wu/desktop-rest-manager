package com.wuxiangknow.rest.task;


import com.wuxiangknow.rest.bean.BetweenTime;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.gui.RestGui;
import com.wuxiangknow.rest.gui.SleepGui;
import com.wuxiangknow.rest.gui.generate.SettingGui;
import com.wuxiangknow.rest.util.DateTimeUtil;
import com.wuxiangknow.rest.util.WindowsUtil;
import org.joda.time.DateTime;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * @Desciption 任务(负责提示用户休息)
 * @Author WuXiang
 * @Date 2019/1/9 22:29
 */
public class RestTimerTask extends TimerTask {

    private SettingGui settingGui;

    public static RestGui restGui;

    public static SleepGui sleepGui;

    public RestGui getRestGui() {
        return restGui;
    }

    public void setRestGui(RestGui restGui) {
        this.restGui = restGui;
    }

    public SleepGui getSleepGui() {
        return sleepGui;
    }

    public void setSleepGui(SleepGui sleepGui) {
        this.sleepGui = sleepGui;
    }

    public RestTimerTask(final SettingGui settingGui) {
        this.settingGui = settingGui;
    }

    //private Thread restThread;

    private Long lastTimeMillis;

    @Override
    public void run() {
        /**
         * 获取当前时间 比较是否超过间隔时间,如果创建休息提示类
         */
        boolean isWork  = true;
        DateTime now = DateTime.now();
        BetweenTime morningBetweenTime = settingGui.getMorningBetweenTime();
        BetweenTime afternoonBetweenTime = settingGui.getAfternoonBetweenTime();
        if(morningBetweenTime != null ){
            isWork = morningBetweenTime.isBetween(now);
        }
        if(!isWork && afternoonBetweenTime!=null){
            isWork = afternoonBetweenTime.isBetween(now);
        }
        if(isWork){
            //如果刚好到了工作的开始时间 需要重置时间
            DateTime morningStartTime = null;
            DateTime afternoonStartTime = null;
            if(morningBetweenTime !=null){
                morningStartTime = morningBetweenTime.getStartTime();
            }
            if(afternoonBetweenTime !=null){
                afternoonStartTime = afternoonBetweenTime.getStartTime();
            }
            updateLastTime(morningStartTime,afternoonStartTime,now);
            if(lastTimeMillis == null){
                lastTimeMillis = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - settingGui.getLastTime() >= settingGui.getMaxWorkTime()
                    && settingGui.isStatus()
                    && !settingGui.isSetting()//调整设置 不考虑 重置lastTime
                    ){
                //超过最高时间 且 状态启用 且 没有正在调整设置
                if(!WindowsUtil.isFullScreen() && ( !DateTimeUtil.isWeekend(new Date())|| !settingGui.isWeekendDisable())
                        && System.currentTimeMillis() - lastTimeMillis < RestConfig.TIMER_REST_TASK_PERIOD*2
                        ){//没有全屏再提示 且 不是周末或者周末可提示 且 两次运行没有超过定时任务周期的2倍时间(防止休眠)
                    //创建休息
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                restGui = new RestGui(settingGui);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    CountDownTask countDownTask = new CountDownTask(restGui);
                    countDownTask.execute();
                    try {
                        countDownTask.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    //休息
                    if(restGui.isStatus()){
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
                                    sleepGui = new SleepGui(settingGui);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        if(settingGui != null){
                            synchronized (settingGui){
                                try {
                                    settingGui.wait(settingGui.getRestTime());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
                                    sleepGui.dispose();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                settingGui.updateTime();
            }
            lastTimeMillis = System.currentTimeMillis();
        }else{
            lastTimeMillis = null;
        }
    }

    private void updateLastTime(DateTime startTime1, DateTime startTime2, DateTime now) {
        updateLastTime(startTime1, now);
        updateLastTime(startTime2, now);
    }

    private void updateLastTime(DateTime startTime, DateTime now) {
        if(startTime ==null){
            startTime = new BetweenTime().getStartTime();
        }
        long startMillis = startTime.getMillisOfDay();
        long nowMillis = now.getMillisOfDay();
        if(Math.abs(startMillis - nowMillis)< RestConfig.TIMER_REST_TASK_PERIOD){
            settingGui.updateTime();
        }
    }

}
