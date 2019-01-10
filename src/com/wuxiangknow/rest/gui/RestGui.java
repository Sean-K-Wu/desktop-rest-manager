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

    private Dimension restGuiSize = new Dimension(100,100);


    public RestGui(SettingGui settingGui) throws HeadlessException {
        this.settingGui = settingGui;
        this.setLayout(null);
        this.setResizable(false);
        this.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width =  (int) restGuiSize.getWidth();
        int height = (int) restGuiSize.getHeight();
        this.setBounds((int)(screenSize.getWidth() - width)/2,(int)(screenSize.getHeight()- height)/2,width,height);
        this.setVisible(false);
    }
}
