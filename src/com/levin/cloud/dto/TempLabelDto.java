package com.levin.cloud.dto;

public class TempLabelDto {
    private double x;
    private double y;
    private double alpha;

    public TempLabelDto(double x, double y, double alpha) {
        this.x = x;
        this.y = y;
        this.alpha = alpha;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
