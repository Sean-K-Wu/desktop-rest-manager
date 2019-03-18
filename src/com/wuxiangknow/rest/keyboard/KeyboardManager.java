package com.wuxiangknow.rest.keyboard;

import com.wuxiangknow.rest.task.RestTimerTask;
import me.coley.simplejna.hook.key.KeyEventReceiver;
import me.coley.simplejna.hook.key.KeyHookManager;

import java.awt.event.KeyEvent;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/3/18 20:48
 */
public class KeyboardManager {



    public static void init(){
        KeyHookManager keyHookManager = new KeyHookManager();
        KeyEventReceiver keyEventReceiver = new KeyEventReceiver(keyHookManager){
            @Override
            public boolean onKeyUpdate(SystemState sysState, PressState pressState, int time, int vkCode) {
                if(pressState.equals(PressState.UP)) {//抬起
                    if(KeyEvent.VK_ESCAPE == vkCode){
                        if(RestTimerTask.restGui !=null && RestTimerTask.restGui.isVisible()){
                            RestTimerTask.restGui.cancel();
                            // 关闭对话框
                            RestTimerTask.restGui.dispose();
                            return true;
                        }
                        if(RestTimerTask.sleepGui !=null && RestTimerTask.sleepGui.isVisible()){
                            RestTimerTask.sleepGui.wakeUp();
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        keyHookManager.hook(keyEventReceiver);
    }
}