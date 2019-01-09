package com.wuxiangknow.rest.gui;


import com.wuxiangknow.rest.keyboard.HotKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

/**
 * 系统托盘UI
 */
public class MainGui extends JFrame{


    private HotKey hotKey;//热键
    private final MainGui parentPanel;
    private PopupMenu pop;
    private MenuItem settingItem ;
    private MenuItem statusItem ;//默认没有开启
    private boolean status;
    private MenuItem exitItem ;
    private long startTime;//开始时间
    private SettingGui settingGui;
    public MainGui() {
        parentPanel=this;
        hotKey = new HotKey(this);//热键
        settingGui = new SettingGui();
        initSystemTray();
    }


    private URL getDefaultTrayImgName(){
        return this.getClass().getResource("/res/icon.png");
    }
    private void initSystemTray() {
        //系统托盘
        final SystemTray tray = SystemTray.getSystemTray();

        pop = new PopupMenu();  //弹出菜单
        settingItem = new MenuItem("设置");
        statusItem = new MenuItem("开启");//默认没有开启
        exitItem = new MenuItem("退出");
        pop.add(settingItem);
        pop.add(statusItem);
        pop.add(exitItem);
        //图标
        ImageIcon trayImg = new ImageIcon(getDefaultTrayImgName());
        final TrayIcon trayIcon = new TrayIcon(trayImg.getImage(), "Rest Manager", pop);
        trayIcon.setImageAutoSize(true);
        settingItem.addActionListener((ActionEvent e)->{


        });
        statusItem.addActionListener((ActionEvent e)->{
            if(!status){
                parentPanel.start();
            }else{
                parentPanel.stop();
            }
        });
        exitItem.addActionListener((ActionEvent e) -> { //
                tray.remove(trayIcon);
                parentPanel.dispose();
                hotKey.destroy();
                System.exit(0);
        });
        try {
            tray.add(trayIcon);
            hotKey.init();
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }

    public void start() {
        status    = true;
        startTime = System.currentTimeMillis();
        statusItem.setName("停止");
    }
    public void stop() {
        status    = false;
        statusItem.setName("开启");
    }
}
