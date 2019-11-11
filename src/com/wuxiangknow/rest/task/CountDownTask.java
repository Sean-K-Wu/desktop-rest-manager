package com.wuxiangknow.rest.task;

import com.wuxiangknow.rest.component.CountDownLabel;
import com.wuxiangknow.rest.config.RestConfig;
import com.wuxiangknow.rest.gui.RestGui;

import javax.swing.*;
import java.util.List;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/3/18 21:43
 */
@Deprecated
public class CountDownTask extends SwingWorker<String,Double> {

    private RestGui restGui;
    private int countDown = RestConfig.COUNTDOWN;

    public CountDownTask(RestGui restGui) {
        this.restGui = restGui;
    }

    @Override
    protected String doInBackground() throws Exception {
        CountDownLabel messageLabel = restGui.getMessageLabel();
        while (countDown >= 0){
            Double[] inAngles = messageLabel.getInAngles();
            for (Double inAngle : inAngles) {
                publish(inAngle);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(countDown > 0){
                Double[] outAngles = messageLabel.getOutAngles();
                for (Double outAngle : outAngles) {
                    publish(outAngle);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            countDown--;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void process(List<Double> chunks) {
        super.process(chunks);
        Double aDouble = chunks.get(chunks.size() - 1);
        CountDownLabel messageLabel = restGui.getMessageLabel();
        messageLabel.setAngle(aDouble);
        if(aDouble.equals(messageLabel.getInAngles()[0]) && countDown >= 0){
            messageLabel.setText(String.valueOf(countDown));
        }
        messageLabel.repaint();
    }

    @Override
    protected void done() {
        super.done();
        // 关闭对话框
        restGui.dispose();
    }
}
