package com.wuxiangknow.rest.gui;

import com.wuxiangknow.rest.config.RestConfig;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Descirption 设置界面
 * @Author WuXiang
 * @Date 2019/1/9/
 */
public class SettingGui extends JFrame {

    private static final long serialVersionUID = 27196777663166828L;

    private long maxWorkTime = RestConfig.MAX_WORK_TIME;//最长连续工作时间  多长工作时间后休息

    private long restTime = RestConfig.REST_TIME;//休息时间

    private Dimension settingSize = new Dimension(500,500);

    private Map<Date,Date> workTimes = new HashMap<>();//工作的时间段

    private long lastTime = System.currentTimeMillis();//活跃开始计算时间

    private transient boolean status ;//后台线程状态
    private Color selectedBackgroundColor = new Color(0 , 66  ,140);
    private Color defaultBackgroundColor = new Color(83 , 83  ,83 );
    private JLabel maxWorkTimeLabel;
    private JTextField maxWorkTimeField;
    private JLabel restTimeLabel;
    private JTextField restTimeField;

    public SettingGui() {
        this.setTitle("设置");
        this.setLayout(null);
        this.setResizable(false);
        this.getContentPane().setBackground(defaultBackgroundColor);
        try {
            this.setIconImage(ImageIO.read(this.getClass().getResource(RestConfig.SETTING_ICON_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxWorkTimeLabel = new JLabel("间隔时间(分)");
        maxWorkTimeField = new JTextField(String.valueOf(maxWorkTime /1000 / 60));
        restTimeLabel    = new JLabel("休息时间(分)");
        restTimeField    = new JTextField(String.valueOf(restTime /1000 / 60));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //界面宽高度
        int width = (int) settingSize.getWidth();
        int height = (int) settingSize.getWidth();
        maxWorkTimeLabel.setBounds(100,100,100,100);
        maxWorkTimeField.setBounds(200,100,100,100);
        //第2行
        restTimeLabel   .setBounds(100,200,100,100);
        restTimeField   .setBounds(200,200,100,100);

        this.add(maxWorkTimeLabel);
        this.add(maxWorkTimeField);
        this.add(restTimeLabel);
        this.add(restTimeField);
        //正中央显示
        this.setBounds((int)(screenSize.getWidth() - settingSize.getWidth())/2,(int)(screenSize.getHeight() - settingSize.getHeight())/2,width,height);
        this.setVisible(false);
    }
    /**
     * 更新时间
     */
    public void updateTime() {
        lastTime = System.currentTimeMillis();
    }

    public long getMaxWorkTime() {
        return maxWorkTime;
    }

    public void setMaxWorkTime(long maxWorkTime) {
        this.maxWorkTime = maxWorkTime;
    }

    public long getRestTime() {
        return restTime;
    }

    public void setRestTime(long restTime) {
        this.restTime = restTime;
    }

    public Map<Date, Date> getWorkTimes() {
        return workTimes;
    }

    public void setWorkTimes(Map<Date, Date> workTimes) {
        this.workTimes = workTimes;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
