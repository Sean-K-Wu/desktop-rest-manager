package com.wuxiangknow.rest.cache;

import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.gui.SettingGui;

import java.io.*;


/**
 * @Desciption 缓存管理
 * @Author WuXiang
 * @Date 2019/1/12 17:31
 */
public class CacheManager {



    public static void save(SettingGui settingGui){
        File file = new File(RestConfig.PROGRAM_CACHE_DIR.concat("\\").concat(RestConfig.SETTING_CACHE_FILE));
        if(!file.exists()){
            File parentFile = file.getParentFile();
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
        }
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(RestConfig.PROGRAM_CACHE_DIR.concat("\\").concat(RestConfig.SETTING_CACHE_FILE)))){
            out.writeObject(new CacheSettingBean(settingGui));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static CacheSettingBean load(){
        File file = new File(RestConfig.PROGRAM_CACHE_DIR.concat("\\").concat(RestConfig.SETTING_CACHE_FILE));
        if(file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(RestConfig.PROGRAM_CACHE_DIR.concat("\\").concat(RestConfig.SETTING_CACHE_FILE)))) {
                return (CacheSettingBean) in.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
