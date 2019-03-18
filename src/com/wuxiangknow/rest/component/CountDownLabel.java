package com.wuxiangknow.rest.component;

import javax.swing.*;
import java.awt.*;

/**
 * @Desciption
 * @Author WuXiang
 * @Date 2019/2/3 11:33
 */
public class CountDownLabel extends JLabel{

    private Double[] inAngles  = new Double[]{90d,60d,-30d,10d,0d};//0.5s
    private Double[] outAngles  = new Double[]{0d,0d,-60d,-90d,-120d};//0.5s
    //private double[] outAngles  = new double[]{0,0,0,-60d,-90};//0.5s

    private double angle;

    public CountDownLabel() {
        this.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        graphics2D.rotate(angle*Math.PI/180,this.getWidth()/2,this.getHeight());
        super.paintComponent(g);
    }

    public void goOut(){
        for (int i = 0; i < outAngles.length; i++) {
            angle = outAngles[i];
            this.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void goIn(){
        for (int i = 0; i < inAngles.length; i++) {
            angle = inAngles[i];
            this.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Double[] getInAngles() {
        return inAngles;
    }

    public void setInAngles(Double[] inAngles) {
        this.inAngles = inAngles;
    }

    public Double[] getOutAngles() {
        return outAngles;
    }

    public void setOutAngles(Double[] outAngles) {
        this.outAngles = outAngles;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
