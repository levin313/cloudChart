package com.levin.cloud.dto;

import java.awt.*;

public class TempSecDto {

    public TempSecDto(double startTemp, double endTemp, Color color) {
        this.startTemp = startTemp;
        this.endTemp = endTemp;
        this.color = color;
    }

    /**
     * 起始温度
     */
    private double startTemp;
    /**
     * 终止温度
     */
    private double endTemp;

    private Color color;

    public double getStartTemp() {
        return startTemp;
    }

    public void setStartTemp(double startTemp) {
        this.startTemp = startTemp;
    }

    public double getEndTemp() {
        return endTemp;
    }

    public void setEndTemp(double endTemp) {
        this.endTemp = endTemp;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
