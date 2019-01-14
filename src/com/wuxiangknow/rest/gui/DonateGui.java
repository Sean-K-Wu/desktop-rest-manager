package com.wuxiangknow.rest.gui;

import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @Desciption 捐赠界面
 * @Author WuXiang
 * @Date 2019/1/14 20:50
 */
public class DonateGui extends JDialog{

    private JLabel aliLabel;
    private JLabel wechatLabel;

    private Dimension imageSize = new Dimension(250,250);
    private Dimension donateSize = new Dimension(525,295);

    public DonateGui() {
        this(null,"打赏",true);
    }

    public DonateGui(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        this.setAlwaysOnTop(true);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        try {
            this.setIconImage(ImageIO.read(this.getClass().getResource(RestConfig.PROGRAM_ICON_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getContentPane().setBackground(RestConfig.DEFAULT_BACKGROUND_COLOR);


        wechatLabel = new JLabel();
        aliLabel    = new JLabel();
        try {
            wechatLabel.setIcon(new ImageIcon(ImageUtil.getScaledImage(ImageIO.read(this.getClass().getResource(RestConfig.DONATE_WECHAT_PATH)),(int)imageSize.getWidth(),(int)imageSize.getHeight())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            aliLabel.setIcon(new ImageIcon(ImageUtil.getScaledImage(ImageIO.read(this.getClass().getResource(RestConfig.DONATE_ALI_PATH)),(int)imageSize.getWidth(),(int)imageSize.getHeight())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        wechatLabel.setBounds(5,5,(int)imageSize.getWidth(),(int)imageSize.getHeight());
        aliLabel.setBounds((int)(donateSize.getWidth()-imageSize.getWidth()-10),5,(int)imageSize.getWidth(),(int)imageSize.getHeight());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //界面宽高度
        int width  = (int)donateSize.getWidth();
        int height = (int)donateSize.getHeight();
        this.add(wechatLabel);
        this.add(aliLabel);
        //正中央显示
        this.setBounds((int)(screenSize.getWidth() - width)/2,(int)(screenSize.getHeight() - height)/2,width,height);
        this.setVisible(true);
    }
}
