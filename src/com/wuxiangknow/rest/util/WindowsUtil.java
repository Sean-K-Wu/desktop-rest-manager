package com.wuxiangknow.rest.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import com.wuxiangknow.rest.config.RestConfig;

import java.awt.*;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/1/12 23:48
 */
public class WindowsUtil {
    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
        WinDef.HWND GetForegroundWindow();
        WinDef.HWND GetDesktopWindow();
        void GetWindowRect(WinDef.HWND hwnd ,WinDef.RECT rect);
    }


    public static boolean isFullScreen()
    {
        WinDef.HWND foregroundWindow = User32.INSTANCE.GetForegroundWindow();
        WinDef.RECT foregroundRectangle = new WinDef.RECT();
        WinDef.RECT desktopWindowRectangle = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect(foregroundWindow, foregroundRectangle);
        WinDef.HWND desktopWindow = User32.INSTANCE.GetDesktopWindow();
        User32.INSTANCE.GetWindowRect(desktopWindow, desktopWindowRectangle);
        Rectangle foreRectangle = foregroundRectangle.toRectangle();
        Rectangle windowRectangle = desktopWindowRectangle.toRectangle();
        return foreRectangle.contains(windowRectangle);
        //return foregroundRectangle.toString().equals(desktopWindowRectangle.toString());
    }



    public static boolean enableAutoBoot(){
        String exePath = System.getProperty("exe.path");
        if(exePath!=null){
            Advapi32Util.registrySetStringValue(HKEY_CURRENT_USER,RestConfig.REG_KEY_PATH,RestConfig.REG_VALUE_NAME,exePath);
            return true;
        }
        return false;
    }

    public static void disableAutoBoot(){
        Advapi32Util.registryDeleteValue(HKEY_CURRENT_USER, RestConfig.REG_KEY_PATH,RestConfig.REG_VALUE_NAME);
    }

}
