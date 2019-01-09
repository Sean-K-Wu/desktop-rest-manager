package com.wuxiangknow.rest.keyboard;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.sun.glass.events.KeyEvent;
import com.wuxiangknow.rest.gui.MainGui;

/**
 * 热键
 */
public class HotKey implements HotkeyListener {

    static final int KEY_1 = 88;

    static final int KEY_2 = 89;

    private MainGui mainGui;

    public HotKey(MainGui mainGui) {
        this.mainGui = mainGui;
    }

    @Override
    public void onHotKey(int i) {
        switch (i){
            case KEY_1://
                mainGui.start();
                break;
            case KEY_2://
                mainGui.stop();
                break;
        }
    }

    public void init(){
        JIntellitype.getInstance().registerHotKey(KEY_1, JIntellitype.MOD_CONTROL,

                KeyEvent.VK_F1);

        JIntellitype.getInstance().registerHotKey(KEY_2, JIntellitype.MOD_CONTROL,

                KeyEvent.VK_F2);

        JIntellitype.getInstance().addHotKeyListener(this);
    }


    public void destroy() {

        JIntellitype.getInstance().unregisterHotKey(KEY_1);

        JIntellitype.getInstance().unregisterHotKey(KEY_2);

    }
}
