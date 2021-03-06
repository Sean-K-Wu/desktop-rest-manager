package com.wuxiangknow.rest.gui;


import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.wuxiangknow.rest.cache.CacheManager;
import com.wuxiangknow.rest.cache.CacheSettingBean;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.gui.generate.SettingGui;
import com.wuxiangknow.rest.keyboard.KeyboardManager;
import com.wuxiangknow.rest.task.RestTimerTask;
import com.wuxiangknow.rest.task.ShutdownTask;
import com.wuxiangknow.rest.task.UpgradeTask;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Timer;

/**
 * 系统托盘UI
 */
public class MainGui {



    private final MainGui parentPanel;
    private PopupMenu pop;
    private MenuItem settingItem ;
    private MenuItem statusItem ;
    private MenuItem helpItem ;
    private MenuItem exitItem ;
    private SettingGui settingGui;
    private TrayIcon trayIcon;

    private SettingStatusListener settingStatusListener;




    public MainGui() {
        initTheme();
        parentPanel=this;
        CacheSettingBean cacheSettingBean = CacheManager.load();
        settingGui = new SettingGui();
        if(cacheSettingBean!=null){
            settingGui.loadCache(cacheSettingBean);
        }
        settingGui.updateComponents();
        if(cacheSettingBean!=null){
            settingGui.initClockTimes(cacheSettingBean.getMorningBetweenTime(),cacheSettingBean.getAfternoonBetweenTime());
        }

        settingStatusListener = new SettingStatusListener();
        settingGui.addPropertyChangeListener("settingStatus",settingStatusListener);
        settingGui.setVisible(true);
        if(!settingGui.requestFocusInWindow()){
            settingGui.requestFocus();
        }
        initSystemTray();
        java.util.Timer timer1 = new Timer(false);
        RestTimerTask restTimerTask = new RestTimerTask(settingGui);
        timer1.schedule(restTimerTask,0,RestConfig.TIMER_REST_TASK_PERIOD);


        java.util.Timer timer2 = new Timer(false);
        UpgradeTask upgradeTask = new UpgradeTask(settingGui, false);
        timer2.schedule(upgradeTask,0,RestConfig.TIMER_UPGRADE_TASK_PERIOD);

        Runtime.getRuntime().addShutdownHook(new ShutdownTask(settingGui));


        KeyboardManager.init(restTimerTask);
    }
    /**
     * 初始化look and feel
     */
    public static void initTheme() {

        try {
            switch (RestConfig.SWING_THEME) {
                case "BeautyEye":
                    BeautyEyeLNFHelper.launchBeautyEyeLNF();
                    //以下语句在BeautyEyeLNFHelper.launchBeautyEyeLNF()之后执行方能起效
                    UIManager.put("TabbedPane.tabAreaInsets"
                            , new javax.swing.plaf.InsetsUIResource(3,0,2,20));
                    UIManager.put("RootPane.setupButtonVisible", false);
                    break;
                case "系统默认":
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case "Windows":
                    UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());
                    break;
                case "Nimbus"://丑
                    UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
                    break;
                case "Metal"://丑
                    UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
                    break;
                case "Motif"://丑
                    UIManager.setLookAndFeel(MotifLookAndFeel.class.getName());
                    break;
                case "Darcula(推荐)":
                    UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
                    break;
                default:
                    UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        trayIcon = new TrayIcon(trayImg.getImage(), RestConfig.PROGRAM_NAME.concat("v").concat(RestConfig.PROGRAM_VERSION), pop);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton() == MouseEvent.BUTTON1){
                    settingGui.setSettingTabSelected();
                    if(!settingGui.isVisible()){
                        settingGui.setVisible(true);
                    }else{
                        settingGui.setState(Frame.NORMAL);
                    }
                }
            }
        });
        settingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingGui.setSettingTabSelected();
                if(!settingGui.isVisible()){
                    settingGui.setVisible(true);
                }else{
                    settingGui.setState(Frame.NORMAL);
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
                System.exit(0);
            }
        });
        try {
            tray.add(trayIcon);

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
        }else {
            settingGui.setStatus(true);
        }
    }



    class SettingStatusListener implements PropertyChangeListener {


        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getPropertyName().equals("settingStatus")){
                if((boolean) evt.getNewValue()){
                    statusItem.setLabel("停止");
                }else{
                    statusItem.setLabel("开启");
                }
            }

        }
    }
}
