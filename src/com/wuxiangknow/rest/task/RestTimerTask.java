package com.wuxiangknow.rest.task;


import com.wuxiangknow.rest.bean.BetweenTime;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.gui.RestGui;
import com.wuxiangknow.rest.gui.SettingGui;
import com.wuxiangknow.rest.gui.SleepGui;
import com.wuxiangknow.rest.util.DateTimeUtil;
import com.wuxiangknow.rest.util.WindowsUtil;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.TimerTask;

/**
 * @Desciption 任务(负责提示用户休息)
 * @Author WuXiang
 * @Date 2019/1/9 22:29
 */
public class RestTimerTask extends TimerTask {

    private SettingGui settingGui;

    public RestTimerTask(SettingGui settingGui) {
        this.settingGui = settingGui;
    }

    //private Thread restThread;

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
            updateLastTime(morningBetweenTime.getStartTime(),afternoonBetweenTime.getStartTime(),now);
            if(System.currentTimeMillis() - settingGui.getLastTime() >= settingGui.getMaxWorkTime()
                    && settingGui.isStatus()
                    && !settingGui.isSetting()//调整设置 不考虑 重置lastTime
                    ){
                //超过最高时间 且 状态启用 且 没有正在调整设置
                if(!WindowsUtil.isFullScreen() && ( !DateTimeUtil.isWeekend(new Date())|| !settingGui.isWeekendDisable())){//没有全屏再提示 且 不是周末或者周末可提示
                    //判断时间段是否满足

                    //创建休息
                    RestGui restGui = new RestGui(settingGui);
                    //休息
                    if(restGui.isStatus()){
                        SleepGui sleepGui = new SleepGui(settingGui);
                        sleepGui.dispose();
                    }
                }
                settingGui.updateTime();
            }
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
        if(Math.abs(startMillis - nowMillis)< RestConfig.TIMER_TASK_PERIOD){
            settingGui.updateTime();
        }
    }

}
