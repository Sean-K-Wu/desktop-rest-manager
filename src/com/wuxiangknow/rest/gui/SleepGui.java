package com.wuxiangknow.rest.gui;

import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Desciption 休息界面
 * @Author WuXiang
 * @Date 2019/1/11 21:42
 */
public class SleepGui extends JFrame{

    private JLabel imageLabel;//图片



    public SleepGui(final SettingGui settingGui) throws HeadlessException {
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
        BufferedImage bufferedImage = null;
        if (settingGui !=null && settingGui.getSleepImagePath() != null ) {
            try {
                //获取该路径
                java.util.List<String> resource = getResource(settingGui.getSleepImagePath());
                if(resource.size()>0){
                    String path = getFileByRandom(resource);
                    bufferedImage = ImageIO.read(new File(path));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(bufferedImage == null){
            String path = getFileByRandom(RestConfig.SLEEP_IMAGE_FILES);
            try {
                bufferedImage = ImageIO.read(this.getClass().getResourceAsStream(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageLabel.setIcon(new ImageIcon(ImageUtil.getScaledImage(bufferedImage,(int) screenSize.getWidth(), (int) screenSize.getHeight())));
        imageLabel.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(KeyEvent.VK_ESCAPE == e.getKeyCode()){
                    synchronized (settingGui){
                        settingGui.notifyAll();
                    }
                }
            }
        });
        this.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        this.add(imageLabel);
        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);//最大化
        if(!this.requestFocusInWindow()){
            this.requestFocus();
        }
        synchronized (settingGui){
            try {
                settingGui.wait(settingGui.getRestTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private String getFileByRandom(List<String> resource) {
        Random random = new Random();
        int i = random.nextInt(resource.size());
        return resource.get(i);
    }
    private String getFileByRandom(String[] resource) {
        Random random = new Random();
        int i = random.nextInt(resource.length);
        return resource[i];
    }
    public java.util.List<String> getResource(String path) {
        return getResourceByFile(path);
    }


    public java.util.List<String> getResourceByFile(String path) {
        ArrayList<String> fileNames = new ArrayList<>();
        File file = new File(path);
        if(file.exists() && file.isDirectory()){
            String name;
            for (File childFile : file.listFiles()) {
                name = childFile.getName();
                if(childFile.isFile() && ImageUtil.isImage(name)){
                    fileNames.add(childFile.getPath());
                }
            }
        }
        return fileNames;
    }
}
