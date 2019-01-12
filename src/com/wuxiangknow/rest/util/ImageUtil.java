package com.wuxiangknow.rest.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @Desciption 图片工具类
 * @Author WuXiang
 * @Date 2019/1/12 15:34
 */
public class ImageUtil {

    public static Image getScaledImage(BufferedImage srcImg, int width, int height){
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, width, height, null);
        g2.dispose();
        return resizedImg;
    }

    public static boolean isImage(String filename){
       return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png");
    }
}
