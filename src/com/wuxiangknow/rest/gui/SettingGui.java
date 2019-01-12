package com.wuxiangknow.rest.gui;

import com.wuxiangknow.rest.cache.CacheManager;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.util.RegUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
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
    private SettingGui settingGui;
    private long maxWorkTime = RestConfig.MAX_WORK_TIME;//最长连续工作时间  多长工作时间后休息

    private long restTime = RestConfig.REST_TIME;//休息时间

    private Dimension settingSize = new Dimension(500,500);

    private Map<Date,Date> workTimes = new HashMap<>();//工作的时间段

    private long lastTime = System.currentTimeMillis();//活跃开始计算时间

    private transient boolean status ;//后台线程状态
    private Color defaultBackgroundColor = new Color(227, 237, 205);
    private JLabel maxWorkTimeLabel;
    private JTextField maxWorkTimeField;
    private JLabel restTimeLabel;
    private JTextField restTimeField;
    private JLabel sleepImagesPathLabel;
    private JTextField sleepImagesPatheField;
    private static final String SLEEP_IMAGE_PATH_DEFAULT = "默认";
    public SettingGui() {
        this.settingGui = this;
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
        sleepImagesPathLabel= new JLabel("图片路径");
        sleepImagesPatheField= new JTextField(SLEEP_IMAGE_PATH_DEFAULT);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //界面宽高度
        int width = (int) settingSize.getWidth();
        int height = (int) settingSize.getWidth();
        maxWorkTimeLabel.setBounds(100,0,100,30);
        maxWorkTimeField.setBounds(200,0,100,30);

        //第2行
        restTimeLabel   .setBounds(100,30,100,30);
        restTimeField   .setBounds(200,30,100,30);

        sleepImagesPathLabel.setBounds(100,60,100,30);
        sleepImagesPatheField.setBounds(200,60,100,30);
        this.add(maxWorkTimeLabel);
        this.add(maxWorkTimeField);
        this.add(restTimeLabel);
        this.add(restTimeField);
        this.add(sleepImagesPathLabel);
        this.add(sleepImagesPatheField);
        //正中央显示
        this.setBounds((int)(screenSize.getWidth() - settingSize.getWidth())/2,(int)(screenSize.getHeight() - settingSize.getHeight())/2,width,height);
        this.setVisible(false);
    }


    public void initListeners(){
        sleepImagesPatheField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                JTextField component = (JTextField) e.getComponent();
                String text = component.getText();
                if(text !=null){
                    if(SLEEP_IMAGE_PATH_DEFAULT.equals(text.trim())){
                        component.setText("");
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                JTextField component = (JTextField) e.getComponent();
                String text = component.getText();
                if(text !=null){
                    File file = new File(text.trim());
                    if(!file.exists()){
                        component.setText(SLEEP_IMAGE_PATH_DEFAULT);
                    }
                }
            }
        });
        maxWorkTimeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                JTextField component = (JTextField) e.getComponent();
                if(component.getText() !=null && RegUtil.isIntegerNumber(component.getText())
                        ){
                    maxWorkTime = Integer.parseInt(component.getText()) * 1000 * 60;
                }else{
                    component.setText(String.valueOf(maxWorkTime /1000 / 60));
                }
            }
        });
        restTimeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                JTextField component = (JTextField) e.getComponent();
                if(component.getText() !=null && RegUtil.isIntegerNumber(component.getText())
                        ){
                    restTime = Integer.parseInt(component.getText()) * 1000 * 60;
                }else{
                    component.setText(String.valueOf(restTime /1000 / 60));
                }
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                super.windowDeactivated(e);
                CacheManager.save(settingGui);
            }
        });
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

    public String getSleepImagePath(){
        return SLEEP_IMAGE_PATH_DEFAULT.equals(sleepImagesPatheField.getText())?RestConfig.SLEEP_IMAGE_DIR:sleepImagesPatheField.getText();
    }
}
