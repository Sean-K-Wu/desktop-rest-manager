package com.wuxiangknow.rest.task;


import com.wuxiangknow.rest.bean.BetweenTime;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.gui.RestGui;
import com.wuxiangknow.rest.gui.SettingGui;
import com.wuxiangknow.rest.gui.SleepGui;
import com.wuxiangknow.rest.util.DateTimeUtil;
import com.wuxiangknow.rest.util.WindowsUtil;
import me.coley.simplejna.hook.key.KeyEventReceiver;
import me.coley.simplejna.hook.key.KeyHookManager;
import org.joda.time.DateTime;

import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.TimerTask;

/**
 * @Desciption 任务(负责提示用户休息)
 * @Author WuXiang
 * @Date 2019/1/9 22:29
 */
public class RestTimerTask extends TimerTask {

    private SettingGui settingGui;

    private RestGui restGui;

    private SleepGui sleepGui;

    public RestTimerTask(final SettingGui settingGui) {
        this.settingGui = settingGui;
        KeyHookManager keyHookManager = new KeyHookManager();
        KeyEventReceiver keyEventReceiver = new KeyEventReceiver(keyHookManager){
            @Override
            public boolean onKeyUpdate(SystemState sysState, PressState pressState, int time, int vkCode) {
                if(pressState.equals(PressState.UP)) {//抬起
                    if(KeyEvent.VK_ESCAPE == vkCode){
                        if(restGui !=null && restGui.isVisible()){
                            restGui.cancel();
                            // 关闭对话框
                            restGui.dispose();
                        }
                        if(sleepGui !=null && sleepGui.isVisible()){
                            sleepGui.wakeUp();
                        }
                    }
                }
                return false;
            }
        };
        keyHookManager.hook(keyEventReceiver);
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
            updateLastTime(morningBetweenTime.getStartTime(),afternoonBetweenTime.getStartTime(),now);
            if(lastTimeMillis == null){
                lastTimeMillis = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - settingGui.getLastTime() >= settingGui.getMaxWorkTime()
                    && settingGui.isStatus()
                    && !settingGui.isSetting()//调整设置 不考虑 重置lastTime
                    ){
                //超过最高时间 且 状态启用 且 没有正在调整设置
                if(!WindowsUtil.isFullScreen() && ( !DateTimeUtil.isWeekend(new Date())|| !settingGui.isWeekendDisable())
                        && System.currentTimeMillis() - lastTimeMillis < RestConfig.TIMER_TASK_PERIOD*2
                        ){//没有全屏再提示 且 不是周末或者周末可提示 且 两次运行没有超过定时任务周期的2倍时间(防止休眠)
                    //创建休息
                    restGui = new RestGui(settingGui);
                    restGui.setVisible(true);
                    //休息
                    if(restGui.isStatus()){
                        sleepGui = new SleepGui(settingGui);
                        sleepGui.dispose();
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
        if(Math.abs(startMillis - nowMillis)< RestConfig.TIMER_TASK_PERIOD){
            settingGui.updateTime();
        }
    }

}
