package com.wuxiangknow.rest.gui;


import com.wuxiangknow.rest.component.CountDownLabel;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.gui.generate.SettingGui;
import com.wuxiangknow.rest.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Desciption 休息界面
 * @Author WuXiang
 * @Date 2019/1/9 23:03
 */
public class RestGui{

    private SettingGui settingGui;

    private Dimension restGuiSize = new Dimension(200,200);

    private  Dimension messageSize = new Dimension(200,100);





    private Color fontColor  =new Color(52,152,219);

    private boolean status = true;

    private JDialog dialog;



    private CountDownLabel messageLabel;

    public RestGui(final SettingGui settingGui)  {
        this.settingGui = settingGui;

        // 创建一个模态对话框
        Frame owner = null;
        dialog = new JDialog(owner, "休息提示", true);
        dialog.setLayout(new FlowLayout());
        dialog.setUndecorated(true);
        dialog.setAlwaysOnTop(true);
        // 设置对话框的宽高
        dialog.setSize(restGuiSize.getSize());
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(null);
        //透明
        dialog.setBackground(new Color(0,0,0,0));
        dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        // 创建一个标签显示消息内容
        messageLabel = new CountDownLabel();
        messageLabel.setFont(new Font("Microsoft YaHei",Font.BOLD,90));
        messageLabel.setPreferredSize(messageSize);
        messageLabel.setForeground(fontColor);
        // 创建一个按钮用于关闭对话框
        JLabel okLabel = new JLabel();
        try {
            BufferedImage bufferedImage = ImageIO.read(this.getClass().getResource(RestConfig.COUNT_DOWN_CANCEL_PATH));
            okLabel.setIcon(new ImageIcon(ImageUtil.getScaledImage(bufferedImage,30,30)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        okLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        okLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                RestGui.this.cancel();
                // 关闭对话框
                RestGui.this.dispose();
            }
        });
        // 添加组件到面板
        dialog.getContentPane().add(messageLabel);
        dialog.getContentPane().add(okLabel);
        dialog.setModal(false);
        if(settingGui!=null){
            settingGui.setVisible(false);
            settingGui.setState(JFrame.NORMAL);
        }
        dialog.setVisible(true);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isVisible(){
        return dialog.isVisible();
    }

    public void cancel() {
        status= false;
        if(settingGui!=null){
            settingGui.updateTime();
        }
    }

    public void dispose() {
        // 关闭对话框
        dialog.dispose();
    }

    public void setVisible(boolean b){
        dialog.setVisible(b);
    }


    public CountDownLabel getMessageLabel() {
        return messageLabel;
    }

    public JDialog getDialog() {
        return dialog;
    }

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

    public void setMessageLabel(CountDownLabel messageLabel) {

        this.messageLabel = messageLabel;
    }
}
