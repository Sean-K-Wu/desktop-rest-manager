package com.wuxiangknow.rest.gui;


import com.wuxiangknow.rest.cache.CacheManager;
import com.wuxiangknow.rest.cache.CacheSettingBean;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.keyboard.HotKey;
import com.wuxiangknow.rest.task.RestTimerTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Timer;

/**
 * 系统托盘UI
 */
public class MainGui extends JFrame{


    private HotKey hotKey;//热键
    private final MainGui parentPanel;
    private PopupMenu pop;
    private MenuItem settingItem ;
    private MenuItem statusItem ;
    private MenuItem helpItem ;
    private MenuItem exitItem ;
    private SettingGui settingGui;
    private TrayIcon trayIcon;


    public MainGui() {
        parentPanel=this;
        CacheSettingBean cacheSettingBean = CacheManager.load();
        settingGui = new SettingGui();
        if(cacheSettingBean!=null){
            settingGui.loadCache(cacheSettingBean);
        }
        settingGui.initCompenents();
        settingGui.initListeners();
        settingGui.initClockTimes();
        hotKey = new HotKey(this);//热键
        initSystemTray();
        java.util.Timer timer = new Timer(false);
        timer.schedule(new RestTimerTask(settingGui),0,RestConfig.TIMER_TASK_PERIOD);
    }


    private URL getDefaultTrayImgName(){
        return this.getClass().getResource(RestConfig.PROGRAM_ICON_PATH);
    }
    private void initSystemTray() {
        //系统托盘
        final SystemTray tray = SystemTray.getSystemTray();

        pop = new PopupMenu();  //弹出菜单
        settingItem = new MenuItem("设置");
        statusItem = new MenuItem(!settingGui.isStatus()?"开启":"停止");//
        helpItem = new MenuItem("帮助");//
        exitItem = new MenuItem("退出");
        addAllItem();
        //图标
        ImageIcon trayImg = new ImageIcon(getDefaultTrayImgName());
        trayIcon = new TrayIcon(trayImg.getImage(), RestConfig.PROGRAM_NAME.concat(RestConfig.PROGRAM_VERSION), pop);
        trayIcon.setImageAutoSize(true);
        settingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!settingGui.isVisible()){
                    settingGui.setVisible(true);
                    if(!settingGui.requestFocusInWindow()){
                        settingGui.requestFocus();
                    }
                }
            }
        });
        statusItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeStatus();
            }
        });

        helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(URI.create(RestConfig.PROGRAM_HELP_URL));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                parentPanel.dispose();
                hotKey.destroy();
                CacheManager.save(settingGui);
                System.exit(0);
            }
        });
        try {
            tray.add(trayIcon);
            hotKey.init();
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }

    private void addAllItem() {
        pop.add(settingItem);
        pop.add(statusItem);
        pop.add(helpItem);
        pop.add(exitItem);
    }



    public void changeStatus(){
        if(settingGui.isStatus()){
            settingGui.setStatus(false);
            statusItem.setLabel("开启");
        }else {
            settingGui.setStatus(true);
            settingGui.updateTime();
            statusItem.setLabel("停止");
        }
    }

}
