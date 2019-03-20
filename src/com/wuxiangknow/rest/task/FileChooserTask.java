package com.wuxiangknow.rest.task;

import com.wuxiangknow.rest.cache.CacheManager;
import com.wuxiangknow.rest.gui.generate.SettingGui;
import com.wuxiangknow.rest.util.ImageUtil;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
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

        if(settingGui.getSleepImagesPatheChooser()==null){
            settingGui.setSleepImagesPatheChooser( new JFileChooser());
            File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
            settingGui.getSleepImagesPatheChooser().setCurrentDirectory(desktopDir);
        }else{
            settingGui.getSleepImagesPatheChooser().setVisible(true);
        }
        if(settingGui.getSleepImagePath() != null){
            settingGui.getSleepImagesPatheChooser().setCurrentDirectory(new File(settingGui.getSleepImagePath()));
        }
        JFileChooser sleepImagesPatheChooser = settingGui.getSleepImagesPatheChooser();

        sleepImagesPatheChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        sleepImagesPatheChooser.setMultiSelectionEnabled(false);
        int result = sleepImagesPatheChooser.showOpenDialog(settingGui);
        sleepImagesPatheChooser.setVisible(false);
        if(JFileChooser.APPROVE_OPTION  == result){
            File selectedFile = sleepImagesPatheChooser.getSelectedFile();
            if(selectedFile != null){
                if(ImageUtil.hasImages(selectedFile) || ImageUtil.isImage(selectedFile.getName())){
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
            if(content !=null && !content.equals(settingGui.getSleepImagesPatheField().getText())){
                if(content.equals(SettingGui.SLEEP_IMAGE_PATH_DEFAULT_VALUE)){
                    settingGui.setSleepImagePath(null);
                    settingGui.getSleepImagesPatheField().setText(SettingGui.SLEEP_IMAGE_PATH_DEFAULT_VALUE);
                }else{
                    settingGui.setSleepImagePath(content);
                    settingGui.getSleepImagesPatheField().setText(content);
                }
                CacheManager.save(settingGui);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
