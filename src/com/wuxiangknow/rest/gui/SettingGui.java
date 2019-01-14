package com.wuxiangknow.rest.gui;

import com.wuxiangknow.rest.bean.BetweenTime;
import com.wuxiangknow.rest.cache.CacheManager;
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



    private BetweenTime morningBetweenTime;
    private BetweenTime afternoonBetweenTime;

    private long lastTime = System.currentTimeMillis();//活跃开始计算时间

    private  boolean status ;//后台线程状态

    private  JLabel maxWorkTimeLabel;
    private  JTextField maxWorkTimeField;
    private  JLabel restTimeLabel;
    private  JTextField restTimeField;
    private  JLabel sleepImagesPathLabel;
    private  JTextField sleepImagesPatheField;

    private  JButton sleepImagesPathButton;
    private  String sleepImagePath ;
    private JFileChooser sleepImagesPatheChooser;

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

    private  JLabel statusLabel;
    private  JButton statusButton;

    private JButton donateButton;
    private DonateGui donateGui;

    private static final String SLEEP_IMAGE_PATH_DEFAULT_VALUE = "默认";
    public SettingGui() {

    }

    public  void handleOpenFileChooser(){
        sleepImagesPatheChooser = new JFileChooser();
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
                    sleepImagePath = null;
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
        this.getContentPane().setBackground(RestConfig.DEFAULT_BACKGROUND_COLOR);
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
        sleepImagesPatheField= new JTextField(sleepImagePath ==null?SLEEP_IMAGE_PATH_DEFAULT_VALUE:sleepImagePath);

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

        morningStartHourBox.initItems(SimpleDateFormat.HOUR0_FIELD);


        weekendLabel = new JLabel("周末禁用");
        weekendCheckBox = new JCheckBox();
        autoBootLabel = new JLabel("开机自启");
        autoBootCheckBox = new JCheckBox();

        statusLabel = new JLabel("状态");
        statusButton = new JButton();
        if(status){
            statusButton.setText("停止");
        }else{
            statusButton.setText("开启");
        }

        donateButton = new JButton("打赏");
        donateButton.setForeground(Color.WHITE);
        donateButton.setBackground(RestConfig.DONATE_BACKGROUND_COLOR);
        maxWorkTimeLabel.setBounds(100,0,100,30);
        maxWorkTimeField.setBounds(200,0,100,30);


        restTimeLabel   .setBounds(100,30,100,30);
        restTimeField   .setBounds(200,30,100,30);

        sleepImagesPathLabel.setBounds(100,60,100,30);
        sleepImagesPatheField.setBounds(200,60,100,30);
        sleepImagesPathButton.setBounds(300,60,100,30);


        morningWorkLabel.setBounds(100,90,100,30);
        morningStartHourBox.setBounds(200,90,50,30);

        morningStartMinuteBox.setBounds(250,90,50,30);
        morningSepereteLabel.setBounds(300,90,18,30);
        morningSepereteLabel.setBackground(RestConfig.DEFAULT_BACKGROUND_COLOR);
        morningEndHourBox.setBounds(318,90,50,30);
        morningEndMinuteBox.setBounds(368,90,50,30);

        afternoonWorkLabel.setBounds(100,120,100,30);
        afternoonStartHourBox.setBounds(200,120,50,30);
        afternoonStartMinuteBox.setBounds(250,120,50,30);
        afternoonSepereteLabel.setBounds(300,120,18,30);
        afternoonSepereteLabel.setBackground(RestConfig.DEFAULT_BACKGROUND_COLOR);
        afternoonEndHourBox.setBounds(318,120,50,30);
        afternoonEndMinuteBox.setBounds(368,120,50,30);

        weekendLabel.setBounds(100,150,100,30);
        weekendCheckBox.setBounds(200,150,100,30);
        weekendCheckBox.setBackground(RestConfig.DEFAULT_BACKGROUND_COLOR);
        autoBootLabel.setBounds(100,180,100,30);
        autoBootCheckBox.setBounds(200,180,100,30);
        autoBootCheckBox.setBackground(RestConfig.DEFAULT_BACKGROUND_COLOR);

        statusLabel.setBounds(100,210,100,30);
        statusButton.setBounds(200,210,100,30);

        donateButton.setBounds((int)(settingSize.getWidth()-100)/2,310,100,30);

        if(autoBoot && WindowsUtil.enableAutoBoot()){
            autoBootCheckBox.setSelected(true);
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
        this.add(statusLabel);
        this.add(statusButton);
        this.add(donateButton);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //界面宽高度
        int width = (int) settingSize.getWidth();
        int height = (int) settingSize.getHeight();
        //正中央显示
        this.setBounds((int)(screenSize.getWidth() - width)/2,(int)(screenSize.getHeight() - height)/2,width,height);
        this.setVisible(false);

    }


    public void initListeners(){
        bindClockListeners(morningStartHourBox,morningStartMinuteBox,morningEndHourBox,morningEndMinuteBox,true);
        bindClockListeners(afternoonStartHourBox,afternoonStartMinuteBox,afternoonEndHourBox,afternoonEndMinuteBox,false);

        donateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                donateGui =  new DonateGui();

            }
        });

        statusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(settingGui.isStatus()){
                    settingGui.setStatus(false);
                }else{
                    settingGui.setStatus(true);
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
                if(autoBootCheckBox.isSelected() ){
                    if(WindowsUtil.enableAutoBoot()){
                        autoBoot = true;
                    }else{
                        autoBootCheckBox.setSelected(false);
                    }
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
                        sleepImagePath = null;
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
                        settingGui.updateTime();
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
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                CacheManager.save(settingGui);
            }
        });
    }

    private void bindClockListeners(final ClockComboBox startHourBox, final ClockComboBox startMinuteBox, final ClockComboBox endHourBox , final ClockComboBox endMinuteBox , final boolean isMorning) {
        startHourBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object obj = e.getItem();
                    if(obj !=null && RegUtil.isIntegerNumber(obj.toString())){
                        //修改之后
                        if(isMorning){
                            startMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD);
                        }else{
                             Integer morningEndHour = (Integer) morningEndHourBox.getSelectedItem();
                             Integer afternoonEndHour = (Integer) e.getItem();
                             if(morningEndHour == afternoonEndHour){
                                 Integer morningEndMinute = (Integer) morningEndMinuteBox.getSelectedItem();
                                 startMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD,morningEndMinute+1);
                             }else{
                                 startMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD);
                             }
                        }
                    }else{
                        startMinuteBox.clearAllItem();
                    }
                }
            }
        });
        startMinuteBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Integer item = null;
                    Object obj = e.getItem();
                    if(obj !=null && RegUtil.isIntegerNumber(obj.toString())){
                        obj = startHourBox.getSelectedItem();
                        item = (Integer) obj;
                        //修改之后
                        endHourBox.initItems(SimpleDateFormat.HOUR0_FIELD,item);
                    }else{
                        endHourBox.clearAllItem();
                    }
                }
            }
        });
        endHourBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object obj = e.getItem();
                    if(obj !=null && RegUtil.isIntegerNumber(obj.toString())){
                        Integer startHour = (Integer) startHourBox.getSelectedItem();
                        Integer endHour = (Integer) endHourBox.getSelectedItem();
                        if(startHour == endHour){
                            Integer startMin= (Integer) startMinuteBox.getSelectedItem();
                            endMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD,startMin+1);
                        }else{
                            endMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD);
                        }
                    }else{
                        endMinuteBox.clearAllItem();
                    }
                }
            }
        });

        endMinuteBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object obj = e.getItem();
                    if(obj !=null && RegUtil.isIntegerNumber(obj.toString())){
                        //存储时间
                        if(isMorning){
                            Integer startHour = (Integer) morningStartHourBox.getSelectedItem();
                            Integer startMinute = (Integer) morningStartMinuteBox.getSelectedItem();
                            Integer endHour = (Integer) morningEndHourBox.getSelectedItem();
                            Integer endMinute = (Integer) morningEndMinuteBox.getSelectedItem();
                            morningBetweenTime = new BetweenTime(startHour,startMinute,endHour,endMinute);
                            afternoonStartHourBox.initItems(SimpleDateFormat.HOUR0_FIELD,endHour);
                        }else{
                            Integer startHour = (Integer) afternoonStartHourBox.getSelectedItem();
                            Integer startMinute = (Integer) afternoonStartMinuteBox.getSelectedItem();
                            Integer endHour = (Integer) afternoonEndHourBox.getSelectedItem();
                            Integer endMinute = (Integer) afternoonEndMinuteBox.getSelectedItem();
                            afternoonBetweenTime = new BetweenTime(startHour,startMinute,endHour,endMinute);
                        }
                    }else{
                        if(isMorning){//需要清除下午时间
                            morningBetweenTime = null;
                            afternoonStartHourBox.clearAllItem();
                        }else{
                            afternoonBetweenTime = null;
                        }
                    }
                }

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
        if(status){
            statusButton.setText("停止");
            updateTime();
        }else{
            statusButton.setText("开始");
        }
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
        this.sleepImagePath = cacheSettingBean.getSleepImagePath();
        this.status = cacheSettingBean.isStatus();
        this.autoBoot = cacheSettingBean.isAutoBoot();
        this.weekendDisable = cacheSettingBean.isWeekendDisable();
        this.morningBetweenTime = cacheSettingBean.getMorningBetweenTime();
        this.afternoonBetweenTime = cacheSettingBean.getAfternoonBetweenTime();
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

    public BetweenTime getMorningBetweenTime() {
        return morningBetweenTime;
    }

    public void setMorningBetweenTime(BetweenTime morningBetweenTime) {
        this.morningBetweenTime = morningBetweenTime;
    }

    public BetweenTime getAfternoonBetweenTime() {
        return afternoonBetweenTime;
    }

    public void setAfternoonBetweenTime(BetweenTime afternoonBetweenTime) {
        this.afternoonBetweenTime = afternoonBetweenTime;
    }

    public void initClockTimes() {
        BetweenTime afternoonBetweenTime = this.afternoonBetweenTime;
        initClockTimeValue(this.morningBetweenTime,morningStartHourBox,morningStartMinuteBox,morningEndHourBox,morningEndMinuteBox);
        initClockTimeValue(afternoonBetweenTime,afternoonStartHourBox,afternoonStartMinuteBox,afternoonEndHourBox,afternoonEndMinuteBox);
    }

    private void initClockTimeValue(BetweenTime betweenTime ,ClockComboBox startHourBox,ClockComboBox startMinuteBox,ClockComboBox endHourBox,ClockComboBox endMinuteBox) {
        if(betweenTime !=null){
            if(betweenTime.getStartTime() !=null){
                startHourBox.setSelectedItem(betweenTime.getStartHour());
                startMinuteBox.setSelectedItem(betweenTime.getStartMinute());
            }
            if(betweenTime.getEndTime() !=null){
                endHourBox.setSelectedItem(betweenTime.getEndHour());
                endMinuteBox.setSelectedItem(betweenTime.getEndMinute());
            }
        }
    }

    public boolean isSetting(){
        return this.isActive() || (sleepImagesPatheChooser!=null && sleepImagesPatheChooser.isVisible()) || (donateGui !=null && donateGui.isVisible());
    }
}
