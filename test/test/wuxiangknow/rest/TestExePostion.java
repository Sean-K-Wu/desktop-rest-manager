package test.wuxiangknow.rest;

import org.junit.Test;

import java.io.File;

/**
 * @Desciption 测试程序执行位置
 * @Author WuXiang
 * @Date 2019/1/13 22:27
 */
public class TestExePostion {

    @Test
    public void test(){
        System.out.println(new File("").getAbsolutePath());
    }
}
