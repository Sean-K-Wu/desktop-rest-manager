/*
 * Created by JFormDesigner on Sat Mar 02 11:05:13 GMT+08:00 2019
 */

package com.wuxiangknow.rest.gui.generate;

import com.wuxiangknow.rest.bean.BetweenTime;
import com.wuxiangknow.rest.cache.CacheSettingBean;
import com.wuxiangknow.rest.component.ClockComboBox;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.util.ImageUtil;
import com.wuxiangknow.rest.util.RegUtil;
import com.wuxiangknow.rest.util.WindowsUtil;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author WuXiang
 */
public class SettingGui extends JFrame {
    PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private  boolean status ;//后台线程状态
    private  boolean autoBoot = true;
    private  boolean weekendDisable = true;
    private BetweenTime morningBetweenTime;
    private BetweenTime afternoonBetweenTime;
    private static final String SLEEP_IMAGE_PATH_DEFAULT_VALUE = "默认";
    private  String sleepImagePath ;
    private JFileChooser sleepImagesPatheChooser;
    private long restTime = RestConfig.REST_TIME;//休息时间
    private long maxWorkTime = RestConfig.MAX_WORK_TIME;//最长连续工作时间  多长工作时间后休息
    private long lastTime = System.currentTimeMillis();//活跃开始计算时间
    public boolean isStatus() {
        return status;
    }

    public boolean isAutoBoot() {
        return autoBoot;
    }
    public boolean isSetting(){
        return this.isActive() || (sleepImagesPatheChooser!=null && sleepImagesPatheChooser.isVisible());
    }
    public void setAutoBoot(boolean autoBoot) {
        this.autoBoot = autoBoot;
    }

    public String getSleepImagePath() {
        return sleepImagePath;
    }

    public void setSleepImagePath(String sleepImagePath) {
        this.sleepImagePath = sleepImagePath;
    }

    public long getRestTime() {
        return restTime;
    }

    public void setRestTime(long restTime) {
        this.restTime = restTime;
    }

    public long getMaxWorkTime() {
        return maxWorkTime;
    }

    public void setMaxWorkTime(long maxWorkTime) {
        this.maxWorkTime = maxWorkTime;
    }

    public boolean isWeekendDisable() {
        return weekendDisable;
    }

    public void setWeekendDisable(boolean weekendDisable) {
        this.weekendDisable = weekendDisable;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
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

    public void setStatus(boolean status) {
        boolean oldValue = this.status;
        this.status = status;
        if(status){
            statusButton.setText("停止");
            updateTime();
        }else{
            statusButton.setText("开始");
        }
        firePropertyChange("settingStatus",oldValue,status);
    }

    public SettingGui() {
        initComponents();
    }

    public void updateComponents() {
        this.setTitle(RestConfig.PROGRAM_NAME);
        try {
            this.setIconImage(ImageIO.read(this.getClass().getResource(RestConfig.PROGRAM_ICON_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        maxWorkTimeField.setText(String.valueOf(maxWorkTime /1000 / 60));
        restTimeField.setText(String.valueOf(restTime /1000 / 60));
        sleepImagesPatheField.setText(sleepImagePath ==null?SLEEP_IMAGE_PATH_DEFAULT_VALUE:sleepImagePath);
        morningStartHourBox.initItems(SimpleDateFormat.HOUR0_FIELD);
        if(status){
            statusButton.setText("停止");
        }else{
            statusButton.setText("开启");
        }
        if(autoBoot && WindowsUtil.enableAutoBoot()){
            autoBootCheckBox.setSelected(true);
        }
        if(weekendDisable){
            weekendCheckBox.setSelected(true);
        }
        this.setBounds(200,200,700,700);
    }



    public void initClockTimes() {
        BetweenTime afternoonBetweenTime = this.afternoonBetweenTime;
        initClockTimeValue(this.morningBetweenTime,morningStartHourBox,morningStartMinuteBox,morningEndHourBox,morningEndMinuteBox);
        initClockTimeValue(this.afternoonBetweenTime,afternoonStartHourBox,afternoonStartMinuteBox,afternoonEndHourBox,afternoonEndMinuteBox);
    }
    private void initClockTimeValue(BetweenTime betweenTime ,ClockComboBox startHourBox,ClockComboBox startMinuteBox,ClockComboBox endHourBox,ClockComboBox endMinuteBox) {
        if(betweenTime !=null){
            if(betweenTime.getStartTime() !=null){
                startHourBox.setSelectedItem(startHourBox.getClockItem(betweenTime.getStartHour()));
                startMinuteBox.setSelectedItem(startMinuteBox.getClockItem(betweenTime.getStartMinute()));
            }
            if(betweenTime.getEndTime() !=null){
                endHourBox.setSelectedItem(endHourBox.getClockItem(betweenTime.getEndHour()));
                endMinuteBox.setSelectedItem(endMinuteBox.getClockItem(betweenTime.getEndMinute()));
            }
        }
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
    public void updateTime() {
        lastTime = System.currentTimeMillis();
    }
    private void maxWorkTimeFieldFocusLost(FocusEvent e) {
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
                this.updateTime();
            }
        }
        component.setText(String.valueOf(maxWorkTime /1000 / 60));
    }

    private void restTimeFieldFocusLost(FocusEvent e) {
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

    private void sleepImagesPathButtonMouseClicked(MouseEvent e) {
        sleepImagesPathButton.setEnabled(false);
        this.handleOpenFileChooser();
        sleepImagesPathButton.setEnabled(true);
    }
    public  void handleOpenFileChooser(){
        sleepImagesPatheChooser = new JFileChooser();
        sleepImagesPatheChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        sleepImagesPatheChooser.setMultiSelectionEnabled(false);
        int result = sleepImagesPatheChooser.showOpenDialog(this);
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
    private void sleepImagesPatheFieldFocusGained(FocusEvent e) {
        JTextField component = (JTextField) e.getComponent();
        String text = component.getText();
        if(text !=null){
            if(SLEEP_IMAGE_PATH_DEFAULT_VALUE.equals(text.trim())){
                component.setText("");
            }
        }
    }

    private void sleepImagesPatheFieldFocusLost(FocusEvent e) {
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
    private void morningStartHourBoxItemStateChanged(ItemEvent e) {
        handleEndHourEvent(e,true);
    }

    private void morningStartMinuteBoxItemStateChanged(ItemEvent e) {
        handleStartMinuteEvent(e,true);
    }

    private void morningEndHourBoxItemStateChanged(ItemEvent e) {
        handleEndHourEvent(e,true);
    }

    private void morningEndMinuteBoxItemStateChanged(ItemEvent e) {
        handleEndMinuteEvent(e,true);
    }

    private void afternoonStartHourBoxItemStateChanged(ItemEvent e) {
        handleStartHourEvent(e,false);
    }

    private void handleStartHourEvent(ItemEvent e ,boolean isMorning) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Object obj = e.getItem();
            if(obj !=null && RegUtil.isIntegerNumber(obj.toString())){
                //修改之后
                if(isMorning){
                    morningStartMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD);
                }else{
                    Integer morningEndHour = Integer.parseInt((String) morningEndHourBox.getSelectedItem());
                    Integer afternoonEndHour = Integer.parseInt((String) e.getItem());
                    if(morningEndHour == afternoonEndHour){
                        Integer morningEndMinute = Integer.parseInt((String) morningEndMinuteBox.getSelectedItem());
                        afternoonStartMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD,morningEndMinute+1);
                    }else{
                        afternoonStartMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD);
                    }
                }
            }else{
                if(isMorning){
                    morningStartMinuteBox.clearAllItem();
                }else{
                    afternoonStartMinuteBox.clearAllItem();
                }
            }
        }
    }

    private void afternoonStartMinuteBoxItemStateChanged(ItemEvent e) {
        handleStartMinuteEvent(e,false);
    }

    private void handleStartMinuteEvent(ItemEvent e, boolean isMorning) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Integer item = null;
            Object obj = e.getItem();
            ClockComboBox startHourBox;
            ClockComboBox endHourBox;
            if(isMorning){
                startHourBox = morningStartHourBox;
                endHourBox = morningEndHourBox;
            }else{
                startHourBox = afternoonStartHourBox;
                endHourBox = afternoonEndHourBox;
            }
            if(obj !=null && RegUtil.isIntegerNumber(obj.toString())){
                obj = startHourBox.getSelectedItem();
                item = Integer.parseInt((String) obj);
                //修改之后
                endHourBox.initItems(SimpleDateFormat.HOUR0_FIELD,item);
            }else{
                endHourBox.clearAllItem();
            }
        }
    }

    private void afternoonEndHourBoxItemStateChanged(ItemEvent e) {
        handleEndHourEvent(e,false);
    }

    private void handleEndHourEvent(ItemEvent e, boolean isMorning) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            ClockComboBox startHourBox;
            ClockComboBox endHourBox;
            ClockComboBox startMinuteBox;
            ClockComboBox endMinuteBox;
            if(isMorning){
                startHourBox = morningStartHourBox;
                endHourBox = morningEndHourBox;
                startMinuteBox = morningStartMinuteBox;
                endMinuteBox = morningEndMinuteBox;
            }else{
                startHourBox = afternoonStartHourBox;
                endHourBox = afternoonEndHourBox;
                startMinuteBox = afternoonStartMinuteBox;
                endMinuteBox = afternoonEndMinuteBox;
            }
            Object obj = e.getItem();
            if(obj !=null && RegUtil.isIntegerNumber(obj.toString())){
                Integer startHour = Integer.parseInt((String) startHourBox.getSelectedItem());
                Integer endHour = Integer.parseInt((String) endHourBox.getSelectedItem());
                if(startHour == endHour){
                    Integer startMin= Integer.parseInt((String) startMinuteBox.getSelectedItem());
                    endMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD,startMin+1);
                }else{
                    endMinuteBox.initItems(SimpleDateFormat.MINUTE_FIELD);
                }
            }else{
                endMinuteBox.clearAllItem();
            }
        }
    }

    private void afternoonEndMinuteBoxItemStateChanged(ItemEvent e) {
        handleEndMinuteEvent(e ,false);
    }

    private void handleEndMinuteEvent(ItemEvent e, boolean isMorning) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Object obj = e.getItem();
            if(obj !=null && RegUtil.isIntegerNumber(obj.toString())){
                //存储时间
                if(isMorning){
                    Integer startHour = Integer.parseInt((String) morningStartHourBox.getSelectedItem());
                    Integer startMinute = Integer.parseInt((String) morningStartMinuteBox.getSelectedItem());
                    Integer endHour = Integer.parseInt((String) morningEndHourBox.getSelectedItem());
                    Integer endMinute = Integer.parseInt((String) morningEndMinuteBox.getSelectedItem());
                    morningBetweenTime = new BetweenTime(startHour,startMinute,endHour,endMinute);
                    afternoonStartHourBox.initItems(SimpleDateFormat.HOUR0_FIELD,endHour);
                }else{
                    Integer startHour = Integer.parseInt((String) afternoonStartHourBox.getSelectedItem());
                    Integer startMinute = Integer.parseInt((String) afternoonStartMinuteBox.getSelectedItem());
                    Integer endHour = Integer.parseInt((String) afternoonEndHourBox.getSelectedItem());
                    Integer endMinute = Integer.parseInt((String) afternoonEndMinuteBox.getSelectedItem());
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

    private void weekendCheckBoxMouseClicked(MouseEvent e) {
        if(weekendCheckBox.isSelected()){
            weekendDisable = true;
        }else {
            weekendDisable = false;
        }
    }

    private void autoBootCheckBoxMouseClicked(MouseEvent e) {
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

    private void statusButtonMouseClicked(MouseEvent e) {
        statusButton.setEnabled(false);
        if(this.isStatus()){
            this.setStatus(false);
        }else{
            this.setStatus(true);
        }
        statusButton.setEnabled(true);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        rootPanel = new JPanel();
        panel1 = new JPanel();
        tabbedPane1 = new JTabbedPane();
        aboutPanle = new JPanel();
        iconLable = new JLabel();
        programNameLable = new JLabel();
        autherLable = new JLabel();
        redPacketLable = new JLabel();
        versionLable = new JLabel();
        updateLable = new JLabel();
        projectLable = new JLabel();
        settingPanel = new JPanel();
        maxWorkTimeLabel = new JLabel();
        maxWorkTimeField = new JTextField();
        restTimeLabel = new JLabel();
        restTimeField = new JTextField();
        sleepImagesPathLabel = new JLabel();
        sleepImagesPatheField = new JTextField();
        sleepImagesPathButton = new JButton();
        morningWorkLabel = new JLabel();
        morningStartHourBox = new ClockComboBox();
        morningStartMinuteBox = new ClockComboBox();
        morningSepereteLabel = new JLabel();
        morningEndHourBox = new ClockComboBox();
        morningEndMinuteBox = new ClockComboBox();
        afternoonWorkLabel = new JLabel();
        afternoonStartHourBox = new ClockComboBox();
        afternoonStartMinuteBox = new ClockComboBox();
        afternoonSepereteLabel = new JLabel();
        afternoonEndHourBox = new ClockComboBox();
        afternoonEndMinuteBox = new ClockComboBox();
        weekendLabel = new JLabel();
        weekendCheckBox = new JCheckBox();
        autoBootLabel = new JLabel();
        autoBootCheckBox = new JCheckBox();
        statusLabel = new JLabel();
        statusButton = new JButton();
        donatePanel = new JPanel();
        wxLable = new JLabel();
        aliLable = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout());

        //======== rootPanel ========
        {
            rootPanel.setLayout(new GridLayout());

            //======== panel1 ========
            {
                panel1.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]",
                    // rows
                    "[]"));

                //======== tabbedPane1 ========
                {
                    tabbedPane1.setRequestFocusEnabled(false);

                    //======== aboutPanle ========
                    {
                        aboutPanle.setLayout(new MigLayout(
                            "insets 0,hidemode 3,gap 0 0",
                            // columns
                            "[grow,fill]" +
                            "[grow,fill]" +
                            "[grow,fill]" +
                            "[grow,fill]" +
                            "[grow,fill]" +
                            "[grow,fill]" +
                            "[grow,fill]",
                            // rows
                            "[grow,fill]"));

                        //---- iconLable ----
                        iconLable.setIcon(null);
                        aboutPanle.add(iconLable, "cell 0 0");

                        //---- programNameLable ----
                        programNameLable.setText("\u4f11\u606f\u5c0f\u7a0b\u5e8f");
                        aboutPanle.add(programNameLable, "cell 1 0");

                        //---- autherLable ----
                        autherLable.setText("Power By \u5434\u7fd4");
                        aboutPanle.add(autherLable, "cell 1 0");
                        aboutPanle.add(redPacketLable, "cell 3 0");

                        //---- versionLable ----
                        versionLable.setText("v1.5");
                        aboutPanle.add(versionLable, "cell 4 0");

                        //---- updateLable ----
                        updateLable.setText("\u68c0\u67e5\u66f4\u65b0");
                        aboutPanle.add(updateLable, "cell 5 0");

                        //---- projectLable ----
                        projectLable.setText("Fork Me");
                        aboutPanle.add(projectLable, "cell 6 0");
                    }
                    tabbedPane1.addTab("\u5173\u4e8e", aboutPanle);

                    //======== settingPanel ========
                    {
                        settingPanel.setLayout(new MigLayout(
                            "hidemode 3",
                            // columns
                            "[fill]" +
                            "[fill]",
                            // rows
                            "[58]" +
                            "[26]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]"));

                        //---- maxWorkTimeLabel ----
                        maxWorkTimeLabel.setText("\u95f4\u9694\u65f6\u957f(\u5206)\uff1a");
                        settingPanel.add(maxWorkTimeLabel, "cell 0 0,alignx leading,growx 0");

                        //---- maxWorkTimeField ----
                        maxWorkTimeField.addFocusListener(new FocusAdapter() {
                            @Override
                            public void focusLost(FocusEvent e) {
                                maxWorkTimeFieldFocusLost(e);
                            }
                        });
                        settingPanel.add(maxWorkTimeField, "cell 1 0");

                        //---- restTimeLabel ----
                        restTimeLabel.setText("\u4f11\u606f\u65f6\u957f(\u5206)\uff1a");
                        settingPanel.add(restTimeLabel, "cell 1 0,alignx left,growx 0");

                        //---- restTimeField ----
                        restTimeField.addFocusListener(new FocusAdapter() {
                            @Override
                            public void focusLost(FocusEvent e) {
                                restTimeFieldFocusLost(e);
                            }
                        });
                        settingPanel.add(restTimeField, "cell 1 0");

                        //---- sleepImagesPathLabel ----
                        sleepImagesPathLabel.setText("\u5c4f\u4fdd\u56fe\u7247\uff1a");
                        settingPanel.add(sleepImagesPathLabel, "cell 0 1,alignx left,growx 0");

                        //---- sleepImagesPatheField ----
                        sleepImagesPatheField.addFocusListener(new FocusAdapter() {
                            @Override
                            public void focusGained(FocusEvent e) {
                                sleepImagesPatheFieldFocusGained(e);
                            }
                            @Override
                            public void focusLost(FocusEvent e) {
                                sleepImagesPatheFieldFocusLost(e);
                            }
                        });
                        settingPanel.add(sleepImagesPatheField, "cell 1 1");

                        //---- sleepImagesPathButton ----
                        sleepImagesPathButton.setText("\u9009\u62e9\u6587\u4ef6/\u6587\u4ef6\u5939");
                        sleepImagesPathButton.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                sleepImagesPathButtonMouseClicked(e);
                            }
                        });
                        settingPanel.add(sleepImagesPathButton, "cell 1 1,alignx right,growx 0");

                        //---- morningWorkLabel ----
                        morningWorkLabel.setText("\u5de5\u4f5c\u65f6\u95f4(\u4e0a\u5348)\uff1a");
                        settingPanel.add(morningWorkLabel, "cell 0 2");

                        //---- morningStartHourBox ----
                        morningStartHourBox.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                morningStartHourBoxItemStateChanged(e);
                            }
                        });
                        settingPanel.add(morningStartHourBox, "cell 1 2");

                        //---- morningStartMinuteBox ----
                        morningStartMinuteBox.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                morningStartMinuteBoxItemStateChanged(e);
                            }
                        });
                        settingPanel.add(morningStartMinuteBox, "cell 1 2");

                        //---- morningSepereteLabel ----
                        morningSepereteLabel.setText("~");
                        settingPanel.add(morningSepereteLabel, "cell 1 2");

                        //---- morningEndHourBox ----
                        morningEndHourBox.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                morningEndHourBoxItemStateChanged(e);
                            }
                        });
                        settingPanel.add(morningEndHourBox, "cell 1 2");

                        //---- morningEndMinuteBox ----
                        morningEndMinuteBox.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                morningEndMinuteBoxItemStateChanged(e);
                            }
                        });
                        settingPanel.add(morningEndMinuteBox, "cell 1 2");

                        //---- afternoonWorkLabel ----
                        afternoonWorkLabel.setText("\u5de5\u4f5c\u65f6\u95f4(\u4e0b\u5348)\uff1a");
                        settingPanel.add(afternoonWorkLabel, "cell 0 3");

                        //---- afternoonStartHourBox ----
                        afternoonStartHourBox.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                afternoonStartHourBoxItemStateChanged(e);
                            }
                        });
                        settingPanel.add(afternoonStartHourBox, "cell 1 3");

                        //---- afternoonStartMinuteBox ----
                        afternoonStartMinuteBox.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                afternoonStartMinuteBoxItemStateChanged(e);
                            }
                        });
                        settingPanel.add(afternoonStartMinuteBox, "cell 1 3");

                        //---- afternoonSepereteLabel ----
                        afternoonSepereteLabel.setText("~");
                        settingPanel.add(afternoonSepereteLabel, "cell 1 3");

                        //---- afternoonEndHourBox ----
                        afternoonEndHourBox.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                afternoonEndHourBoxItemStateChanged(e);
                            }
                        });
                        settingPanel.add(afternoonEndHourBox, "cell 1 3");

                        //---- afternoonEndMinuteBox ----
                        afternoonEndMinuteBox.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                afternoonEndMinuteBoxItemStateChanged(e);
                            }
                        });
                        settingPanel.add(afternoonEndMinuteBox, "cell 1 3");

                        //---- weekendLabel ----
                        weekendLabel.setText("\u5468\u672b\u7981\u7528\uff1a");
                        settingPanel.add(weekendLabel, "cell 0 4");

                        //---- weekendCheckBox ----
                        weekendCheckBox.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                weekendCheckBoxMouseClicked(e);
                            }
                        });
                        settingPanel.add(weekendCheckBox, "cell 1 4");

                        //---- autoBootLabel ----
                        autoBootLabel.setText("\u5f00\u673a\u81ea\u542f\uff1a");
                        settingPanel.add(autoBootLabel, "cell 1 4");

                        //---- autoBootCheckBox ----
                        autoBootCheckBox.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                autoBootCheckBoxMouseClicked(e);
                            }
                        });
                        settingPanel.add(autoBootCheckBox, "cell 1 4");

                        //---- statusLabel ----
                        statusLabel.setText("\u72b6\u6001\uff1a");
                        settingPanel.add(statusLabel, "cell 0 5");

                        //---- statusButton ----
                        statusButton.setText("\u5f00\u542f");
                        statusButton.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                statusButtonMouseClicked(e);
                            }
                        });
                        settingPanel.add(statusButton, "cell 1 5");
                    }
                    tabbedPane1.addTab("\u8bbe\u7f6e", settingPanel);

                    //======== donatePanel ========
                    {
                        donatePanel.setLayout(new MigLayout(
                            "hidemode 3",
                            // columns
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]",
                            // rows
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]"));
                        donatePanel.add(wxLable, "cell 1 1 6 6");
                        donatePanel.add(aliLable, "cell 9 1 6 6");
                    }
                    tabbedPane1.addTab("\u6350\u8d60", donatePanel);
                }
                panel1.add(tabbedPane1, "cell 0 0");
            }
            rootPanel.add(panel1);
        }
        contentPane.add(rootPanel);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel rootPanel;
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JPanel aboutPanle;
    private JLabel iconLable;
    private JLabel programNameLable;
    private JLabel autherLable;
    private JLabel redPacketLable;
    private JLabel versionLable;
    private JLabel updateLable;
    private JLabel projectLable;
    private JPanel settingPanel;
    private JLabel maxWorkTimeLabel;
    private JTextField maxWorkTimeField;
    private JLabel restTimeLabel;
    private JTextField restTimeField;
    private JLabel sleepImagesPathLabel;
    private JTextField sleepImagesPatheField;
    private JButton sleepImagesPathButton;
    private JLabel morningWorkLabel;
    private ClockComboBox morningStartHourBox;
    private ClockComboBox morningStartMinuteBox;
    private JLabel morningSepereteLabel;
    private ClockComboBox morningEndHourBox;
    private ClockComboBox morningEndMinuteBox;
    private JLabel afternoonWorkLabel;
    private ClockComboBox afternoonStartHourBox;
    private ClockComboBox afternoonStartMinuteBox;
    private JLabel afternoonSepereteLabel;
    private ClockComboBox afternoonEndHourBox;
    private ClockComboBox afternoonEndMinuteBox;
    private JLabel weekendLabel;
    private JCheckBox weekendCheckBox;
    private JLabel autoBootLabel;
    private JCheckBox autoBootCheckBox;
    private JLabel statusLabel;
    private JButton statusButton;
    private JPanel donatePanel;
    private JLabel wxLable;
    private JLabel aliLable;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
