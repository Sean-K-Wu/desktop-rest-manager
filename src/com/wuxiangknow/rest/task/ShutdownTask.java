package com.wuxiangknow.rest.task;

import com.wuxiangknow.rest.cache.CacheManager;
import com.wuxiangknow.rest.gui.generate.SettingGui;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/3/18 21:15
 */
public class ShutdownTask extends Thread{

    private SettingGui settingGui;

    public ShutdownTask(SettingGui settingGui) {
        this.settingGui = settingGui;
    }

    @Override
    public void run() {
        super.run();
        CacheManager.save(settingGui);
    }
}
