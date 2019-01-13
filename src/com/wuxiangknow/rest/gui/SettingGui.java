package com.wuxiangknow.rest.gui;

import com.wuxiangknow.rest.cache.CacheSettingBean;
import com.wuxiangknow.rest.component.ClockComboBox;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.util.ImageUtil;
import com.wuxiangknow.rest.util.RegUtil;
import com.wuxiangknow.rest.util.WindowsUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    private  boolean status ;//后台线程状态
    private  Color defaultBackgroundColor = new Color(227, 237, 205);
    private  JLabel maxWorkTimeLabel;
    private  JTextField maxWorkTimeField;
    private  JLabel restTimeLabel;
    private  JTextField restTimeField;
    private  JLabel sleepImagesPathLabel;
    private  JTextField sleepImagesPatheField;

    private  JButton sleepImagesPathButton;
    private  String sleepImagePath = RestConfig.SLEEP_IMAGE_DIR;


    private  JLabel morningWorkLabel;
    private ClockComboBox morningStartHourBox;
    private ClockComboBox morningEndHourBox;
    private  JLabel morningSepereteLabel;
    private ClockComboBox morningStartMinuteBox;
    private ClockComboBox morningEndMinuteBox;

    private  JLabel afternoonWorkLabel;
    private ClockComboBox afternoonStartHourBox;
    private ClockComboBox afternoonEndHourBox;
    private  JLabel afternoonSepereteLabel;
    private ClockComboBox afternoonStartMinuteBox;
    private ClockComboBox afternoonEndMinuteBox;



    private  JLabel autoBootLabel;
    private  JCheckBox autoBootCheckBox;
    private  boolean autoBoot = true;

    private  JLabel weekendLabel;
    private  JCheckBox weekendCheckBox;
    private  boolean weekendDisable = true;

    private static final String SLEEP_IMAGE_PATH_DEFAULT_VALUE = "默认";
    public SettingGui() {

    }

    public  void handleOpenFileChooser(){
        JFileChooser sleepImagesPatheChooser = new JFileChooser();
        sleepImagesPatheChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        sleepImagesPatheChooser.setMultiSelectionEnabled(false);
        int result = sleepImagesPatheChooser.showOpenDialog(settingGui);
        if(JFileChooser.APPROVE_OPTION  == result){
            File selectedFile = sleepImagesPatheChooser.getSelectedFile();
            if(selectedFile != null){
                boolean hasImages = hasImages(selectedFile);
                if(hasImages){
                    String absolutePath = selectedFile.getAbsolutePath();
                    sleepImagesPatheField.setText(absolutePath);
                    sleepImagePath = absolutePath;
                }else{
                    sleepImagesPatheField.setText(SLEEP_IMAGE_PATH_DEFAULT_VALUE);
                    sleepImagePath = RestConfig.SLEEP_IMAGE_DIR;
                }
            }
        }
    }

    private boolean hasImages(File selectedFile) {
        boolean hasImages = false;
        if(selectedFile.exists() && selectedFile.isDirectory()){
            for (File file : selectedFile.listFiles()) {
                if(file.isFile() && ImageUtil.isImage(file.getName())){
                    hasImages = true;
                    break;
                }
            }
        }
        return hasImages;
    }

    public void initCompenents(){
        this.settingGui = this;
        this.setTitle("设置".concat(RestConfig.PROGRAM_VERSION));
        this.setLayout(null);
        this.setResizable(false);
        this.getContentPane().setBackground(defaultBackgroundColor);
        try {
            this.setIconImage(ImageIO.read(this.getClass().getResource(RestConfig.PROGRAM_ICON_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        maxWorkTimeLabel = new JLabel("间隔时间(分)");
        maxWorkTimeField = new JTextField(String.valueOf(maxWorkTime /1000 / 60));
        restTimeLabel    = new JLabel("休息时间(分)");
        restTimeField    = new JTextField(String.valueOf(restTime /1000 / 60));
        sleepImagesPathLabel= new JLabel("图片路径");
        sleepImagesPatheField= new JTextField(sleepImagePath.equals(RestConfig.SLEEP_IMAGE_DIR)?SLEEP_IMAGE_PATH_DEFAULT_VALUE:sleepImagePath);

        sleepImagesPathButton = new JButton("选择文件夹");

        morningWorkLabel= new JLabel("工作时间(AM)");
        morningSepereteLabel = new JLabel("～");
        morningStartHourBox = new ClockComboBox();
        morningEndHourBox = new ClockComboBox();
        morningStartMinuteBox = new ClockComboBox();
        morningEndMinuteBox = new ClockComboBox();
        afternoonWorkLabel= new JLabel("工作时间(PM)");
        afternoonSepereteLabel = new JLabel("～");
        afternoonStartHourBox = new ClockComboBox();
        afternoonEndHourBox = new ClockComboBox();
        afternoonStartMinuteBox = new ClockComboBox();
        afternoonEndMinuteBox = new ClockComboBox();

        weekendLabel = new JLabel("周末禁用");
        weekendCheckBox = new JCheckBox();
        autoBootLabel = new JLabel("开机自启");
        autoBootCheckBox = new JCheckBox();

        maxWorkTimeLabel.setBounds(100,0,100,30);
        maxWorkTimeField.setBounds(200,0,100,30);


        restTimeLabel   .setBounds(100,30,100,30);
        restTimeField   .setBounds(200,30,100,30);

        sleepImagesPathLabel.setBounds(100,60,100,30);
        sleepImagesPatheField.setBounds(200,60,100,30);
        sleepImagesPathButton.setBounds(300,60,100,30);


        morningWorkLabel.setBounds(100,90,100,30);
        morningStartHourBox.setBounds(200,90,50,30);
        morningStartHourBox.initItems(SimpleDateFormat.HOUR0_FIELD);
        morningStartMinuteBox.setBounds(250,90,50,30);
        morningSepereteLabel.setBounds(300,90,18,30);
        morningSepereteLabel.setBackground(defaultBackgroundColor);
        morningEndHourBox.setBounds(318,90,50,30);
        morningEndMinuteBox.setBounds(368,90,50,30);

        afternoonWorkLabel.setBounds(100,120,100,30);
        afternoonStartHourBox.setBounds(200,120,50,30);
        afternoonStartMinuteBox.setBounds(250,120,50,30);
        afternoonSepereteLabel.setBounds(300,120,18,30);
        afternoonSepereteLabel.setBackground(defaultBackgroundColor);
        afternoonEndHourBox.setBounds(318,120,50,30);
        afternoonEndMinuteBox.setBounds(368,120,50,30);

        weekendLabel.setBounds(100,150,100,30);
        weekendCheckBox.setBounds(200,150,100,30);
        weekendCheckBox.setBackground(defaultBackgroundColor);
        autoBootLabel.setBounds(100,180,100,30);
        autoBootCheckBox.setBounds(200,180,100,30);
        autoBootCheckBox.setBackground(defaultBackgroundColor);
        if(autoBoot){
            autoBootCheckBox.setSelected(true);
            WindowsUtil.enableAutoBoot();
        }
        if(weekendDisable){
            weekendCheckBox.setSelected(true);
        }


        this.add(maxWorkTimeLabel);
        this.add(maxWorkTimeField);
        this.add(restTimeLabel);
        this.add(restTimeField);
        this.add(sleepImagesPathLabel);
        this.add(sleepImagesPatheField);
        this.add(sleepImagesPathButton);

        this.add(morningWorkLabel);
        this.add(morningStartHourBox);
        this.add(morningStartMinuteBox);
        this.add(morningSepereteLabel);
        this.add(morningEndHourBox);
        this.add(morningEndMinuteBox);

        this.add(afternoonWorkLabel);
        this.add(afternoonStartHourBox);
        this.add(afternoonStartMinuteBox);
        this.add(afternoonSepereteLabel);
        this.add(afternoonEndHourBox);
        this.add(afternoonEndMinuteBox);


        this.add(weekendLabel);
        this.add(weekendCheckBox);
        this.add(autoBootLabel);
        this.add(autoBootCheckBox);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //界面宽高度
        int width = (int) settingSize.getWidth();
        int height = (int) settingSize.getWidth();
        //正中央显示
        this.setBounds((int)(screenSize.getWidth() - width)/2,(int)(screenSize.getHeight() - height)/2,width,height);
        this.setVisible(false);

    }


    public void initListeners(){
        morningStartHourBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Integer item = (Integer) e.getItem();
                    //修改之后
                    morningStartMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD);
                }
            }
        });
        morningStartMinuteBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Integer item = (Integer) morningStartHourBox.getSelectedItem();
                    //修改之后
                    morningEndHourBox.initItems(SimpleDateFormat.HOUR0_FIELD,item);
                }
            }
        });
        morningEndHourBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Integer item = (Integer) morningEndHourBox.getSelectedItem();
                    //修改之后
                    morningEndHourBox.initItems(SimpleDateFormat.HOUR0_FIELD,item);
                }
            }
        });



        weekendCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(weekendCheckBox.isSelected()){
                    weekendDisable = true;
                }else {
                    weekendDisable = false;
                }
            }
        });
        autoBootCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(autoBootCheckBox.isSelected()){
                    autoBoot = true;
                    WindowsUtil.enableAutoBoot();
                }else {
                    autoBoot = false;
                    WindowsUtil.disableAutoBoot();
                }
            }
        });


        sleepImagesPathButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                settingGui.handleOpenFileChooser();
            }
        });


        sleepImagesPatheField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                JTextField component = (JTextField) e.getComponent();
                String text = component.getText();
                if(text !=null){
                    if(SLEEP_IMAGE_PATH_DEFAULT_VALUE.equals(text.trim())){
                        component.setText("");
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                JTextField component = (JTextField) e.getComponent();
                String text = component.getText();
                if(text !=null && !text.equals(SLEEP_IMAGE_PATH_DEFAULT_VALUE)){
                    File file = new File(text.trim());
                    if(!hasImages(file)){
                        component.setText(SLEEP_IMAGE_PATH_DEFAULT_VALUE);
                        sleepImagePath = RestConfig.SLEEP_IMAGE_DIR;
                    }else{
                        //如果存在
                        sleepImagePath = text.trim();
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
                    int newMaxWorkTime = 0;
                    try {
                        newMaxWorkTime = Integer.parseInt(component.getText()) * 1000 * 60;
                    } catch (NumberFormatException e1) {
                    }
                    if(newMaxWorkTime>0){
                        maxWorkTime = newMaxWorkTime;
                    }
                }
                component.setText(String.valueOf(maxWorkTime /1000 / 60));

            }
        });
        restTimeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                JTextField component = (JTextField) e.getComponent();
                if(component.getText() !=null && RegUtil.isIntegerNumber(component.getText())
                        ){
                    int newRestTime = 0;
                    try {
                        newRestTime = Integer.parseInt(component.getText()) * 1000 * 60;
                    } catch (NumberFormatException e1) {
                    }
                    if(newRestTime>0){
                        restTime = newRestTime;
                    }
                }
                component.setText(String.valueOf(restTime /1000 / 60));

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

    public String getSleepImagePath() {
        return sleepImagePath;
    }

    public void setSleepImagePath(String sleepImagePath) {
        this.sleepImagePath = sleepImagePath;
    }

    public void loadCache(CacheSettingBean cacheSettingBean) {
        this.maxWorkTime = cacheSettingBean.getMaxWorkTime();
        this.restTime = cacheSettingBean.getRestTime();
        this.workTimes = cacheSettingBean.getWorkTimes();
        this.sleepImagePath = cacheSettingBean.getSleepImagePath();
        this.status = cacheSettingBean.isStatus();
        this.autoBoot = cacheSettingBean.isAutoBoot();
        this.weekendDisable = cacheSettingBean.isWeekendDisable();
    }

    public boolean isAutoBoot() {
        return autoBoot;
    }

    public void setAutoBoot(boolean autoBoot) {
        this.autoBoot = autoBoot;
    }

    public boolean isWeekendDisable() {
        return weekendDisable;
    }

    public void setWeekendDisable(boolean weekendDisable) {
        this.weekendDisable = weekendDisable;
    }
}
