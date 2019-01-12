package com.wuxiangknow.rest.gui;

import com.wuxiangknow.rest.config.RestConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Desciption 休息界面
 * @Author WuXiang
 * @Date 2019/1/9 23:03
 */
public class RestGui{

    private SettingGui settingGui;

    private Dimension restGuiSize = new Dimension(200,100);

    private  Dimension messageSize = new Dimension(200,50);

    private int countDown = RestConfig.COUNTDOWN;

    private static final String PROMPT = "%d秒后进入休息";

    private boolean status = true;

    public RestGui(SettingGui settingGui)  {
        this.settingGui = settingGui;
        // 创建一个模态对话框
        Frame owner = null;
        final JDialog dialog = new JDialog(owner, "休息提示", true);
        dialog.setUndecorated(true);
        dialog.getRootPane ().setOpaque (false);
        dialog.getContentPane ().setBackground (new Color (0, 0, 0, 0));
        dialog.setBackground (new Color (0, 0, 0, 0));
        dialog.setAlwaysOnTop(true);
        // 设置对话框的宽高
        dialog.setSize(restGuiSize.getSize());
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(null);
        // 创建一个标签显示消息内容
        JLabel messageLabel = new JLabel(String.format(PROMPT,countDown),JLabel.CENTER);
        messageLabel.setPreferredSize(messageSize);
        messageLabel.setOpaque(false);
        // 创建一个按钮用于关闭对话框
        JButton okBtn = new JButton("取消");
        okBtn.addActionListener((ActionEvent e)-> {
            // 关闭对话框
            dialog.dispose();
            settingGui.updateTime();
            status= false;
        });
        // 创建对话框的内容面板, 在面板内可以根据自己的需要添加任何组件并做任意是布局
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        // 添加组件到面板
        panel.add(messageLabel);
        panel.add(okBtn);

        // 设置对话框的内容面板
        dialog.setContentPane(panel);
        Thread thread =new Thread( () ->{
            while (countDown >0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDown--;
                messageLabel.setText(String.format(PROMPT,countDown));
            }
            // 关闭对话框
            dialog.dispose();
        });
        thread.start();
        dialog.setVisible(true);
        /*this.setLayout(null);
        this.setResizable(false);
        this.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width =  (int) restGuiSize.getWidth();
        int height = (int) restGuiSize.getHeight();
        this.setBounds((int)(screenSize.getWidth() - width)/2,(int)(screenSize.getHeight()- height)/2,width,height);
        this.setVisible(false);*/

    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
