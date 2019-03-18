package com.wuxiangknow.rest.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Desciption 线程池管理
 * @Author WuXiang
 * @Date 2019/3/18 14:17
 */
public class ThreadPoolManager {

    private  static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void execute(Runnable command){
        executorService.execute(command);
    }

    public static Future<?> submit(Runnable command){
        return executorService.submit(command);
    }
}
