package com.levin.cloud.dto;

public class PointTempDto {
    private double x;
    private double y;
    private double T;
    private double alpha;

    public PointTempDto(double x, double y, double t, double alpha) {
        this.x = x;
        this.y = y;
        T = t;
        this.alpha = alpha;
    }

    public PointTempDto() {
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

    public double getT() {
        return T;
    }

    public void setT(double t) {
        T = t;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
