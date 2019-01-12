package com.wuxiangknow.rest.task;


import com.wuxiangknow.rest.gui.RestGui;
import com.wuxiangknow.rest.gui.SettingGui;
import com.wuxiangknow.rest.gui.SleepGui;
import com.wuxiangknow.rest.util.DateTimeUtil;
import com.wuxiangknow.rest.util.WindowsUtil;

import java.util.Date;
import java.util.Map;
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
        if(System.currentTimeMillis() - settingGui.getLastTime() >= settingGui.getMaxWorkTime()
                && settingGui.isStatus()
                && !settingGui.isActive()//调整设置 不考虑 重置lastTime
                ){
            //超过最高时间 且 状态启用 且 没有正在调整设置
            if(!WindowsUtil.isFullScreen()){//没有全屏再提示
                //判断时间段是否满足
                Map<Date, Date> workTimes = settingGui.getWorkTimes();
                boolean isWork  = true;
                Date now = new Date();
                for (Date date : workTimes.keySet()) {
                    if(date != null){
                        if(!DateTimeUtil.compare(now,date)){
                            isWork = false;
                            break;
                        }else if(workTimes.get(date) != null && !DateTimeUtil.compare(workTimes.get(date),now)){
                            isWork = false;
                            break;
                        }
                    }
                }
                if(isWork){
                    //创建休息
                    RestGui restGui = new RestGui(settingGui);
                    //休息
                    if(restGui.isStatus()){
                        SleepGui sleepGui = new SleepGui(settingGui);
                        sleepGui.dispose();
                    }
                }
            }
            settingGui.updateTime();
        }
    }
}
