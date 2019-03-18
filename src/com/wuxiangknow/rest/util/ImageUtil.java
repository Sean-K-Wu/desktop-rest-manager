package com.wuxiangknow.rest.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Desciption 图片工具类
 * @Author WuXiang
 * @Date 2019/1/12 15:34
 */
public class ImageUtil {

    public static  boolean hasImages(File selectedFile) {
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
    public static Image getScaledImage(BufferedImage srcImg, int width, int height){
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, width, height, null);
        g2.dispose();
        return resizedImg;
    }

    public static boolean isImage(String filename){
        if(filename !=null){
            int i = filename.lastIndexOf(".");
            if(i>0){
                String typeName = filename.substring(i);
                return typeName.equalsIgnoreCase(".jpg") || typeName.equalsIgnoreCase(".jpeg") || typeName.equalsIgnoreCase(".png");
            }
        }
        return false;
    }


}
