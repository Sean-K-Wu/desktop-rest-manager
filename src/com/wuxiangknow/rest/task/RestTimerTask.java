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
import java.awt.image.BufferedImage;
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

    private Double resetTimeMaxRadix = 0.125;//重置工作时间八分之一

    private Long fullScreenStartMillis;
    private Long unFullScreenStartMillis;

    private SettingGui settingGui;

    private RestGui restGui;

    private SleepGui sleepGui;

    public RestGui getRestGui() {
        return restGui;
    }



    public SleepGui getSleepGui() {
        return sleepGui;
    }


    public RestTimerTask(final SettingGui settingGui) {
        this.settingGui = settingGui;
    }

    //private Thread restThread;

    private Long lastTimeMillis;//程序上次运行时间

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
        if(isWork){//是否为工作中
            //如果刚好到了工作的开始时间 需要重置时间
            resetTimeByWorkTime(now, morningBetweenTime, afternoonBetweenTime);
            if(lastTimeMillis == null){ //为null 则初始化
                lastTimeMillis = System.currentTimeMillis();
            }
            //长时间全屏或者休息也需要重置时间
            resetTimeByOff();
            if(checkWhetherSleep()){//是否休息
                if(allowSleep()){
                    //创建休息
                    executeSleep();
                }
            }
            lastTimeMillis = System.currentTimeMillis();
        }else{
            lastTimeMillis = null;
        }
    }

    private void resetTimeByOff() {
        if(settingGui.isWeekendDisable()){  //周末禁用
            if(DateTimeUtil.isWeekend(new Date())){//周末
                settingGui.updateTime();//更新活动时间
            }
        }
        if(windowsIsOff(lastTimeMillis)){//休眠
            settingGui.updateTime();//更新活动时间
        }
        if(WindowsUtil.isFullScreen()){//全屏
            if(fullScreenStartMillis == null){
                fullScreenStartMillis = System.currentTimeMillis();
            }else{
                if(windowsIsOff(fullScreenStartMillis)){
                    settingGui.updateTime();//更新活动时间
                    //重新开始计算
                    fullScreenStartMillis = System.currentTimeMillis();
                    //非全屏时间置为null
                    unFullScreenStartMillis = null;
                }
            }
        }else {
            if(unFullScreenStartMillis == null){
                unFullScreenStartMillis = System.currentTimeMillis();
            }
            if(windowsIsOff(unFullScreenStartMillis)){
                fullScreenStartMillis = null;
            }
        }
    }

    private boolean allowSleep() {
        if(settingGui.isWeekendDisable()){  //周末禁用
            if(DateTimeUtil.isWeekend(new Date())){//周末
                return false;
            }
        }
        if(windowsIsOff(lastTimeMillis)){//休眠
            return false;
        }
        if(WindowsUtil.isFullScreen()){//全屏
            return false;
        }
        return true;
    }

    private boolean windowsIsOff(long timeMillis) {
        return  System.currentTimeMillis() - timeMillis > RestConfig.TIMER_REST_TASK_PERIOD * 2 && System.currentTimeMillis() - timeMillis >= settingGui.getMaxWorkTime() * resetTimeMaxRadix;
    }




    public void executeSleep() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    restGui = new RestGui(settingGui);
                }
            });
            CountDownTask countDownTask = new CountDownTask(restGui);
            countDownTask.execute();
            final BufferedImage sleepBufferedImage =  settingGui.getSleepRandomBufferedImage();
            try {
                countDownTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //休息
            if(restGui.isStatus() && !WindowsUtil.isFullScreen()){
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        sleepGui = new SleepGui(settingGui,sleepBufferedImage);
                    }
                });
                if(settingGui != null){
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime <  settingGui.getRestTime() && !sleepGui.isDisposing()){
                        synchronized (settingGui){
                            try {
                                settingGui.wait(settingGui.getRestTime()-(System.currentTimeMillis() - startTime));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(sleepGui.isVisible()){
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            sleepGui.dispose();
                        }
                    });
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //更新活动时间
        settingGui.updateTime();
    }

    private boolean checkWhetherSleep() {
        return System.currentTimeMillis() - settingGui.getLastTime() >= settingGui.getMaxWorkTime()
                && settingGui.isStatus()
                && !settingGui.isSetting()
                ;
    }

    private void resetTimeByWorkTime(DateTime now, BetweenTime morningBetweenTime, BetweenTime afternoonBetweenTime) {
        DateTime morningStartTime = null;
        DateTime afternoonStartTime = null;
        if(morningBetweenTime !=null){
            morningStartTime = morningBetweenTime.getStartTime();
        }
        if(afternoonBetweenTime !=null){
            afternoonStartTime = afternoonBetweenTime.getStartTime();
        }
        updateLastTime(morningStartTime,afternoonStartTime,now);
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
