package com.wuxiangknow.rest.task;

import com.alibaba.fastjson.JSON;
import com.wuxiangknow.rest.bean.Version;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.gui.generate.SettingGui;
import com.wuxiangknow.rest.util.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Desciption 升级
 * @Author WuXiang
 * @Date 2019/3/18 14:42
 */
public class UpgradeTask implements Runnable{

    private SettingGui settingGui;

    public UpgradeTask(SettingGui settingGui) {
        this.settingGui = settingGui;
    }

    @Override
    public void run() {
        byte[] download = HttpClientUtil.download(RestConfig.CHECK_VERSION_URL,settingGui);
        if(download != null){
            try {
                String content = new String(download, "UTF-8");
                Version version = JSON.parseObject(content, Version.class);
                String latestVersion = version.getVersion();
                String currentVersion = RestConfig.PROGRAM_VERSION;
                if(latestVersion.compareTo(currentVersion)>0){
                    int isPush = JOptionPane.showConfirmDialog(settingGui,
                            "惊现最新版本，是否要更新?", "提示",
                            JOptionPane.YES_NO_OPTION);
                    if (isPush == JOptionPane.YES_OPTION) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.browse(new URI(RestConfig.PROGRAM_HELP_URL));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
