package test.wuxiangknow.rest;

import com.wuxiangknow.rest.util.WindowsUtil;
import org.junit.Test;

/**
 * @Desciption 测试注册表增加与删除
 * @Author WuXiang
 * @Date 2019/1/13 21:59
 */
public class TestReg {
    @Test
    public void testEnable(){
        WindowsUtil.enableAutoBoot();
    }

    @Test
    public void testDisable(){
        WindowsUtil.disableAutoBoot();
    }
}
