package com.wuxiangknow.rest.config;

/**
 * @Desciption 配置信息
 * @Author WuXiang
 * @Date 2019/1/9 21:37
 */
public class RestConfig {

    public static final String PROGRAM_NAME = "休息小程序";

    public static final String TRAY_ICON_PATH = "/res/icon.png";//系统托盘图标

    public static final String SETTING_ICON_PATH = "/res/icon.png";//设置界面图标

    public static final long MAX_WORK_TIME = 40 * 60 * 1000;//最长连续工作时间  多长工作时间后休息

    public static final long REST_TIME = 1 * 60 * 1000;//休息时间

    public static final long TIMER_TASK_PERIOD = 10 * 1000;//任务检查周期
}
