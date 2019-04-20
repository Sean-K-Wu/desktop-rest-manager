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
                if(RestTimerTask.restGui !=null && RestTimerTask.restGui.isVisible() && KeyEvent.VK_ESCAPE == vkCode){
                    if(pressState.equals(PressState.UP)) {//抬起
                        RestTimerTask.restGui.cancel();
                        // 关闭对话框
                        RestTimerTask.restGui.dispose();
                    }
                    return true;
                }
                //KeyEvent.VK_ALT == 18  左alt ==164  右alt==165
                //KeyEvent.VK_WINDOWS==524 左win ==91 右win == 92
                if(RestTimerTask.sleepGui !=null && RestTimerTask.sleepGui.isVisible()
                        && (KeyEvent.VK_ALT == vkCode ||164 == vkCode || 165 ==vkCode
                            || KeyEvent.VK_WINDOWS == vkCode||91 == vkCode || 92 ==vkCode
                            || KeyEvent.VK_ESCAPE == vkCode
                        )
                        ){//屏蔽
                    if( KeyEvent.VK_ESCAPE == vkCode && pressState.equals(PressState.UP)) {//抬起
                        RestTimerTask.sleepGui.wakeUp();
                    }
                    return true;
                }
                return false;
            }
        };
        keyHookManager.hook(keyEventReceiver);
    }
}
