package com.wuxiangknow.rest.keyboard;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.wuxiangknow.rest.gui.MainGui;

/**
 * 热键
 */
public class HotKey implements HotkeyListener {

    static final int KEY_1 = 88;



    private MainGui mainGui;

    public HotKey(MainGui mainGui) {
        this.mainGui = mainGui;
    }

    @Override
    public void onHotKey(int i) {
        switch (i){
            case KEY_1://
                mainGui.changeStatus();
                break;
        }
    }

    public void init(){
        JIntellitype.getInstance().registerHotKey(KEY_1,

                "CTRL+SHIFT+F12");


        JIntellitype.getInstance().addHotKeyListener(this);
    }


    public void destroy() {

        JIntellitype.getInstance().unregisterHotKey(KEY_1);



    }
}
