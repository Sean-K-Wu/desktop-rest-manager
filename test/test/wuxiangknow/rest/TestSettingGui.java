package test.wuxiangknow.rest;

import com.wuxiangknow.rest.gui.SettingGui;
import org.junit.Test;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/1/11 20:49
 */
public class TestSettingGui {

    @Test
    public void test(){
        SettingGui settingGui = new SettingGui();
        settingGui.setVisible(true);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
