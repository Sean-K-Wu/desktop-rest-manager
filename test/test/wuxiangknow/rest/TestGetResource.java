package test.wuxiangknow.rest;

import com.wuxiangknow.rest.gui.SleepGui;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/1/12 14:26
 */
public class TestGetResource {
    @Test
    public void test() throws MalformedURLException {
        SleepGui sleepGui = new SleepGui(null);
        sleepGui.getResourceByJar(new File("C:\\Users\\x7430\\Desktop\\desktop-rest-manager.jar!/res").toURL());
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
