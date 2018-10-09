package com.levin.cloud.dto;

import java.util.UUID;


public class CloudChartDto {


    private UUID uuid;
    /**
     * 名称
     */
    private String name;

    /**
     * 类型（水平/垂直）
     */
    private String type;

    /**
     * 第几层
     */
    private int floorNum;

    private int totalFloor;

    private String path;
    /**
     * 时间
     */
    private String time;
    /**
     * x方向长度
     */
    private Double lengthx;
    /**
     * y方向长度
     */
    private Double lengthy;
    /**
     * z方向高度
     */
    private Double lengthh;
    /**
     * x方向传感器数量
     */
    private int xnum;
    /**
     * y方向传感器数量
     */
    private int ynum;
    /**
     * z方向传感器数数量
     */
    private int znum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getLengthx() {
        return lengthx;
    }

    public void setLengthx(Double lengthx) {
        this.lengthx = lengthx;
    }

    public Double getLengthy() {
        return lengthy;
    }

    public void setLengthy(Double lengthy) {
        this.lengthy = lengthy;
    }

    public Double getLengthh() {
        return lengthh;
    }

    public void setLengthh(Double lengthh) {
        this.lengthh = lengthh;
    }

    public int getXnum() {
        return xnum;
    }

    public void setXnum(int xnum) {
        this.xnum = xnum;
    }

    public int getYnum() {
        return ynum;
    }

    public void setYnum(int ynum) {
        this.ynum = ynum;
    }

    public int getZnum() {
        return znum;
    }

    public void setZnum(int znum) {
        this.znum = znum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] buildPath() {
        String[] paths = new String[this.znum];
        for (int i = 1; i <= znum; i++) {
            String path = this.path + this.name + "\\" + this.time + "\\" + "水平" + i + "截面.png";
            paths[i - 1] = path;
        }
        return paths;
    }

    public String buildTitle() {
        return name + time + type + floorNum  + "截面";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public int getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(int totalFloor) {
        this.totalFloor = totalFloor;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
