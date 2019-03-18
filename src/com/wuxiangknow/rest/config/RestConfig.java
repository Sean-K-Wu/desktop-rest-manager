package com.wuxiangknow.rest.config;

import com.alibaba.fastjson.JSON;
import com.wuxiangknow.rest.bean.Version;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Desciption 配置信息
 * @Author WuXiang
 * @Date 2019/1/9 21:37
 */
public class RestConfig {

    public static final String PROGRAM_NAME = "休息小程序";

    public static  String PROGRAM_VERSION;
    static {
        try {
            Path path = Paths.get(RestConfig.class.getClassLoader().getResource("version.json").toURI());
            byte[] bytes = Files.readAllBytes(path);
            String s = new String(bytes, "UTF-8");
            Version version = JSON.parseObject(s, Version.class);
            PROGRAM_VERSION = version.getVersion();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static final String REG_KEY_PATH = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";
    public static final String REG_VALUE_NAME = "desktop-rest-manager";

    public static final String PROGRAM_CACHE_DIR = System.getProperty("user.home")+"\\AppData\\Local\\desktop-rest-manager";

    public static final String PROGRAM_HELP_URL = "https://github.com/Sean-K-Wu/desktop-rest-manager";

    public static final String SETTING_CACHE_FILE = "setting";

    public static final String PROGRAM_ICON_PATH = "/images/icon.png";//系统托盘图标

    public static final String[] SLEEP_IMAGE_FILES = new String[]{"/images/sleep/sleep.jpg"};//休息显示图片路径

    public static final long MAX_WORK_TIME = 40 * 60 * 1000;//最长连续工作时间  多长工作时间后休息
    //public static final long MAX_WORK_TIME = 10 * 1000;//最长连续工作时间  多长工作时间后休息

    public static final long REST_TIME = 1 * 60 * 1000;//休息时间

    public static final long TIMER_TASK_PERIOD = 10 * 1000;//任务检查周期

    public static final int COUNTDOWN = 3;//倒计时

    public static final  Color DEFAULT_BACKGROUND_COLOR = new Color(227, 237, 205);
    public static final  Color DONATE_BACKGROUND_COLOR = new Color(244,67,54);

    public static final String DONATE_WECHAT_PATH = "/images/donate/wechatpay.jpg";
    public static final String DONATE_ALI_PATH = "/images/donate/alipay.jpg";

    public static final String GIFT_MONEY_PATH = "/images/gift/gift.jpg";

    public static final String COUNT_DOWN_CANCEL_PATH = "/images/cancel.png";

    public static final String SWING_THEME = "BeautyEye";

    /**
     * 软件版本检查url
     */
    public final static String CHECK_VERSION_URL = "https://raw.githubusercontent.com/Sean-K-Wu/desktop-rest-manager/master/resources/version.json";

}
