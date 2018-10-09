package com.levin.cloud.dto;

import com.levin.cloud.contour.Global.PointD;

public class AreaHighPointDto {
    private double temprature;
    private PointD point;

    public AreaHighPointDto() {
    }

    public AreaHighPointDto(double temprature, PointD point) {
        this.temprature = temprature;
        this.point = point;
    }

    public double getTemprature() {
        return temprature;
    }

    public void setTemprature(double temprature) {
        this.temprature = temprature;
    }

    public PointD getPoint() {
        return point;
    }

    public void setPoint(PointD point) {
        this.point = point;
    }
}
