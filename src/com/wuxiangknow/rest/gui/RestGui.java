package com.wuxiangknow.rest.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @Desciption 休息界面
 * @Author WuXiang
 * @Date 2019/1/9 23:03
 */
public class RestGui extends JFrame{

    private SettingGui settingGui;




    public RestGui(SettingGui settingGui) throws HeadlessException {
        this.settingGui = settingGui;
        this.setLayout(null);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setLocation(0,0);
        this.setSize(500, 250);
        this.setVisible(true);
    }
}
