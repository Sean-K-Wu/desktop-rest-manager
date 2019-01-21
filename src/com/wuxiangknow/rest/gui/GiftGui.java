package com.wuxiangknow.rest.gui;

import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @Desciption 红包界面
 * @Author WuXiang
 * @Date 2019/1/14 20:50
 */
public class GiftGui extends JDialog{

    private JLabel giftLabel;


    private Dimension imageSize = new Dimension(400,400);
    private Dimension giftSize = new Dimension(415,445);

    public GiftGui() {
        this(null,"领红包",true);
    }

    public GiftGui(Frame owner, String title, boolean modal) {
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


        giftLabel = new JLabel();

        try {
            giftLabel.setIcon(new ImageIcon(ImageUtil.getScaledImage(ImageIO.read(this.getClass().getResource(RestConfig.GIFT_MONEY_PATH)),(int)imageSize.getWidth(),(int)imageSize.getHeight())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        giftLabel.setBounds(5,5,(int)imageSize.getWidth(),(int)imageSize.getHeight());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //界面宽高度
        int width  = (int)giftSize.getWidth();
        int height = (int)giftSize.getHeight();
        this.add(giftLabel);
        //正中央显示
        this.setBounds((int)(screenSize.getWidth() - width)/2,(int)(screenSize.getHeight() - height)/2,width,height);
        this.setVisible(true);
    }
}
