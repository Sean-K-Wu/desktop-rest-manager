package com.wuxiangknow.rest.util;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Desciption 文件工具类
 * @Author WuXiang
 * @Date 2019/4/21 0:10
 */
public class FileUtil {
    public static String getFileByRandom(List<String> resource) {
        Random random = new Random();
        int i = random.nextInt(resource.size());
        return resource.get(i);
    }
    public static String getFileByRandom(String[] resource) {
        Random random = new Random();
        int i = random.nextInt(resource.length);
        return resource[i];
    }
    public static java.util.List<String> getResource(String path) {
        if(path.indexOf("!")>=0){
            return getResourceByJar( path);
        }else {
            return getResourceByFile( path);
        }
    }


    public static java.util.List<String> getResourceByJar(String path) {
        ArrayList<String> fileNames = new ArrayList<>();
        try {
            String jarPath = path.substring(path.indexOf("/"), path.indexOf("!"));
            String dirPath = path.substring(path.indexOf("!")+2);//去除首个/
            JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()){
                JarEntry jarEntry = entries.nextElement();
                if(!jarEntry.isDirectory() &&  jarEntry.getName().indexOf(dirPath)>=0 && ImageUtil.isImage(jarEntry.getName())){
                    fileNames.add("/".concat(jarEntry.getName()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    public static java.util.List<String> getResourceByFile(String path) {
        ArrayList<String> fileNames = new ArrayList<>();
        File file = new File(path);
        if(file.exists()){
            if( file.isDirectory()){
                String name;
                for (File childFile : file.listFiles()) {
                    name = childFile.getName();
                    if(childFile.isFile() && ImageUtil.isImage(name)){
                        fileNames.add(childFile.getPath());
                    }
                }
            }else if(ImageUtil.isImage(file.getName())){
                fileNames.add(file.getPath());
            }
        }
        return fileNames;
    }
}
