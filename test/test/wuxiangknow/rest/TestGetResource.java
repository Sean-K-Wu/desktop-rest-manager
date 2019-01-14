package test.wuxiangknow.rest;

import com.wuxiangknow.rest.gui.SleepGui;
import org.junit.Test;

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
        //废弃 不再尝试从jar包获取图片
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
