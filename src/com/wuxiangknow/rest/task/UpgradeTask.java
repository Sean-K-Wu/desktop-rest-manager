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
    private boolean isPrompt;

    public UpgradeTask(SettingGui settingGui,boolean isPrompt) {
        this.settingGui = settingGui;
        this.isPrompt =isPrompt;
    }

    @Override
    public void run() {
        byte[] download = HttpClientUtil.doGet(RestConfig.CHECK_VERSION_URL,settingGui,isPrompt);
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
                            URI uri ;
                            if(version.getUrl() == null || version.getUrl().trim().length()==0){
                                uri = new URI(RestConfig.PROGRAM_HELP_URL);
                            }else {
                                uri = new URI(version.getUrl());
                            }
                            desktop.browse(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(isPrompt){
                    JOptionPane.showConfirmDialog(settingGui,"暂无最新版本","提示",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
