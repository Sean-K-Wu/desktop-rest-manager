package com.wuxiangknow.rest.task;

import com.wuxiangknow.rest.gui.generate.SettingGui;
import com.wuxiangknow.rest.util.ImageUtil;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/3/18 17:10
 */
public class FileChooserTask extends SwingWorker<String,String>{

    private SettingGui settingGui;

    public FileChooserTask(SettingGui settingGui) {
        this.settingGui = settingGui;
    }

    @Override
    protected String doInBackground() throws Exception {
        settingGui.setSleepImagesPatheChooser( new JFileChooser());

        JFileChooser sleepImagesPatheChooser = settingGui.getSleepImagesPatheChooser();
        sleepImagesPatheChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        sleepImagesPatheChooser.setMultiSelectionEnabled(false);
        int result = sleepImagesPatheChooser.showOpenDialog(settingGui);
        if(JFileChooser.APPROVE_OPTION  == result){
            File selectedFile = sleepImagesPatheChooser.getSelectedFile();
            if(selectedFile != null){
                boolean hasImages = ImageUtil.hasImages(selectedFile);
                if(hasImages){
                    String absolutePath = selectedFile.getAbsolutePath();
                    return absolutePath;
                }else{
                    return SettingGui.SLEEP_IMAGE_PATH_DEFAULT_VALUE;
                }
            }
        }
        return null;
    }

    @Override
    protected void done() {
        super.done();
        try {
            String content = get();
            if(content !=null){
                if(content.equals(SettingGui.SLEEP_IMAGE_PATH_DEFAULT_VALUE)){
                    settingGui.setSleepImagePath(null);
                    settingGui.getSleepImagesPatheField().setText(SettingGui.SLEEP_IMAGE_PATH_DEFAULT_VALUE);
                }else{
                    settingGui.setSleepImagePath(content);
                    settingGui.getSleepImagesPatheField().setText(content);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
