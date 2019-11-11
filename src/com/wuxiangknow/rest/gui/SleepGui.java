package com.wuxiangknow.rest.gui;

import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.gui.generate.SettingGui;
import com.wuxiangknow.rest.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Desciption 休息界面
 * @Author WuXiang
 * @Date 2019/1/11 21:42
 */
public class SleepGui extends JFrame implements ActionListener {

    private JLabel imageLabel;//图片

    private SettingGui settingGui;

    private static final int ANIMATION_FRAMES = 100;
    private static final int ANIMATION_INTERVAL = 10;

    private int frameIndex;
    // 时钟
    private Timer showTimer;
    private Timer disposeTimer;

    private volatile boolean isDisposing =false;




    public SleepGui(final SettingGui settingGui,BufferedImage bufferedImage) throws HeadlessException {
        this.settingGui = settingGui;
        this.setLayout(null);
        this.setResizable(false);//不可改变大小
        this.setUndecorated(true);//无标题栏
        this.setAlwaysOnTop(true);

        this.setLocationRelativeTo(null);
        try {
            this.setIconImage(ImageIO.read(this.getClass().getResource(RestConfig.PROGRAM_ICON_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        imageLabel = new JLabel();

        imageLabel.setIcon(new ImageIcon(ImageUtil.getScaledImage(bufferedImage,(int) screenSize.getWidth(), (int) screenSize.getHeight())));
        imageLabel.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        /*this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(KeyEvent.VK_ESCAPE == e.getKeyCode()){
                    synchronized (settingGui){
                        settingGui.notifyAll();
                    }
                }
            }
        });*/
        //this.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        this.add(imageLabel);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);//最大化
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        if(settingGui!=null && settingGui.getState() == JFrame.ICONIFIED){
            settingGui.setVisible(false);
            settingGui.setState(JFrame.NORMAL);
        }
        this.setVisible(true);
        if(!SleepGui.this.isActive()){
            SleepGui.this.setState(JFrame.NORMAL);
        }
    }



    public void wakeUp() {
        synchronized (settingGui){
            settingGui.notifyAll();
        }
    }

    @Override
    public void paint(Graphics g) {
        if (isAnimating()) {
            // 根据当前帧显示当前透明度的内容组件
            float alpha = (float) frameIndex / (float) ANIMATION_FRAMES;
            if(isDisposing){
                alpha = 1 -alpha;
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, alpha));
            // Renderer渲染机制
            super.paint(g2d);
        } else {
            // 如果是第一次，启动动画时钟
            if(!isDisposing){
                frameIndex = 0;
                showTimer = new Timer(ANIMATION_INTERVAL, this);
                showTimer.start();
            }else {
                disposeTimer = new Timer(ANIMATION_INTERVAL, this);
                disposeTimer.start();
            }
        }
    }

    public  void close(){
        isDisposing = true;
        closeTimer();
        this.repaint();
        dispose();
    }



    // 判断当前是否正在进行动画
    private boolean isAnimating() {
        return isShowAnimating()|| isDisposeAnimating();
    }
    private boolean isShowAnimating() {
        return showTimer != null && showTimer.isRunning();
    }
    // 判断当前是否正在进行动画
    private boolean isDisposeAnimating() {
        return disposeTimer != null && disposeTimer.isRunning();
    }
    // 关闭时钟，重新初始化
    private void closeTimer() {
        if (isShowAnimating()) {
            showTimer.stop();
            showTimer = null;
        }
        if(isDisposeAnimating()){
            disposeTimer.stop();
            disposeTimer = null;
        }
        if(frameIndex != 0 ){
            frameIndex = ANIMATION_FRAMES -frameIndex;
            if(frameIndex <0){
                frameIndex = 0;
            }
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        // 前进一帧
        frameIndex++;
        if (frameIndex >= ANIMATION_FRAMES ){
            // 最后一帧，关闭动画
            frameIndex = 0;
            closeTimer();
            if(isDisposing){
                super.dispose();
            }
        }
        else
            // 更新当前一帧
            repaint();
    }
}
