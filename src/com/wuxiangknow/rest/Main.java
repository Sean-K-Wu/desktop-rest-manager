package com.wuxiangknow.rest;

import com.wuxiangknow.rest.gui.MainGui;

import javax.swing.*;

/**
 * 启动程序
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGui();
            }
        });
    }
}
