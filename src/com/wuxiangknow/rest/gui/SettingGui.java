package com.wuxiangknow.rest.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Descirption 设置界面
 * @Author WuXiang
 * @Date 2019/1/9/
 */
public class SettingGui extends JPanel {

    private static final long serialVersionUID = 27196777663166828L;

    private long maxWorkTime = 40 * 1000;//最长连续工作时间  多长工作时间后休息

    private long restTime = 1 * 1000;//休息时间

    private Dimension settingSize = new Dimension(500,500);

    private Map<Date,Date> workTimes = new HashMap<>();//工作的时间段

    private JLabel maxWorkTimeLabel;
    private JTextField maxWorkTimeField;
    private JLabel restTimeLabel;
    private JTextField restTimeField;

    public SettingGui() {
        maxWorkTimeLabel = new JLabel("间隔时间(分)");
        maxWorkTimeField = new JTextField(String.valueOf(maxWorkTime /1000 / 60));
        restTimeLabel    = new JLabel("休息时间(分)");
        restTimeField = new JTextField(String.valueOf(restTime /1000 / 60));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        maxWorkTimeLabel.setBounds(100,100,100,100);
        maxWorkTimeField.setBounds(100,100,100,100);
        restTimeLabel.setBounds(100,200,100,100);
        restTimeField.setBounds(100,200,100,100);

        this.add(maxWorkTimeLabel);
        this.add(maxWorkTimeField);
        this.add(restTimeLabel);
        this.add(restTimeField);
        this.setSize(settingSize);
        this.setLocation((int)(screenSize.getWidth() - screenSize.getWidth())/2,(int)(screenSize.getHeight() - screenSize.getHeight())/2);
    }

}
