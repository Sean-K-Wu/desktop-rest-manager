package com.wuxiangknow.rest.keyboard;

import com.wuxiangknow.rest.task.RestTimerTask;
import me.coley.simplejna.hook.key.KeyEventReceiver;
import me.coley.simplejna.hook.key.KeyHookManager;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/3/18 20:48
 */
public class KeyboardManager {


    private static RestTimerTask restTimerTask;


    public static void init(final RestTimerTask restTimerTask){
        KeyboardManager.restTimerTask = restTimerTask;
        KeyHookManager keyHookManager = new KeyHookManager();
        KeyEventReceiver keyEventReceiver = new KeyEventReceiver(keyHookManager){
            @Override
            public boolean onKeyUpdate(SystemState sysState, PressState pressState, int time, int vkCode) {
                if(restTimerTask.getRestGui() !=null && restTimerTask.getRestGui().isVisible() && KeyEvent.VK_ESCAPE == vkCode){
                    if(pressState.equals(PressState.UP)) {//抬起
                        restTimerTask.getRestGui().cancel();
                        // 关闭对话框
                        restTimerTask.getRestGui().dispose();
                    }
                    return true;
                }
                //KeyEvent.VK_ALT == 18  左alt ==164  右alt==165
                //KeyEvent.VK_WINDOWS==524 左win ==91 右win == 92
                if(restTimerTask.getSleepGui() !=null && restTimerTask.getSleepGui().isVisible()
                        && (KeyEvent.VK_ALT == vkCode ||164 == vkCode || 165 ==vkCode
                            || KeyEvent.VK_WINDOWS == vkCode||91 == vkCode || 92 ==vkCode
                            || KeyEvent.VK_ESCAPE == vkCode
                        )
                        ){//屏蔽
                    if( KeyEvent.VK_ESCAPE == vkCode && pressState.equals(PressState.UP)) {//抬起
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
                                    restTimerTask.getSleepGui().dispose();
                                    restTimerTask.getSleepGui().wakeUp();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
                return false;
            }
        };
        keyHookManager.hook(keyEventReceiver);
    }
}
