package test.wuxiangknow.rest;

import com.wuxiangknow.rest.gui.RestGui;
import com.wuxiangknow.rest.task.CountDownTask;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * @Desciption 测试休息界面
 * @Author WuXiang
 * @Date 2019/1/10 0:10
 */
public class TestRestGui {


    public static void main(String[] args) {
        RestGui restGui = new RestGui(null);
        CountDownTask countDownTask = new CountDownTask(restGui);
        countDownTask.execute();
        try {
            String s = countDownTask.get();
            System.out.println("结束:"+s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //restGui.setVisible(true);
    }

    @Test
    public void test(){
        RestGui restGui = new RestGui(null);
        restGui.setVisible(true);
    }
}
