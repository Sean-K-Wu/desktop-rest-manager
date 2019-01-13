package com.wuxiangknow.rest.config;

/**
 * @Desciption 配置信息
 * @Author WuXiang
 * @Date 2019/1/9 21:37
 */
public class RestConfig {

    public static final String PROGRAM_NAME = "休息小程序";

    public static final String PROGRAM_VERSION = "v1.0";

    public static final String REG_KEY_PATH = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";
    public static final String REG_VALUE_NAME = "desktop-rest-manager";

    public static final String PROGRAM_CACHE_DIR = System.getProperty("user.home")+"\\AppData\\Local\\desktop-rest-manager";

    public static final String PROGRAM_HELP_URL = "https://github.com/Sean-K-Wu/desktop-rest-manager";

    public static final String SETTING_CACHE_FILE = "setting";

    public static final String PROGRAM_ICON_PATH = "/res/icon.png";//系统托盘图标

    public static final String SLEEP_IMAGE_DIR = "/res/sleep";//休息显示图片路径

    public static final long MAX_WORK_TIME = 40 *60 * 1000;//最长连续工作时间  多长工作时间后休息

    public static final long REST_TIME = 1 * 60 * 1000;//休息时间

    public static final long TIMER_TASK_PERIOD = 10 * 1000;//任务检查周期
    public static final int COUNTDOWN = 3;//任务检查周期(秒)
}
