package test.wuxiangknow.rest;

import com.wuxiangknow.rest.util.WindowsUtil;
import org.junit.Test;

/**
 * @Desciption 测试屏幕是否全屏
 * @Author WuXiang
 * @Date 2019/1/12 18:09
 */
public class TestIsFullScreen {



    @Test
    public void test(){
        while (true){
            System.out.println(WindowsUtil.isFullScreen());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
