package test.wuxiangknow.rest;

import com.wuxiangknow.rest.config.RestConfig;
import org.junit.Test;

/**
 * @Desciption 测试用户当前目录（放缓存）
 * @Author WuXiang
 * @Date 2019/1/12 17:28
 */
public class TestUserDir {
    @Test
    public void test(){
        System.out.println(RestConfig.PROGRAM_CACHE_DIR);
    }
}
