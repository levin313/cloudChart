package com.levin.cloud.excel.dto;


public class Header {
    /**
     * 标题
     */
    private String title;


    /**
     * 粮食品种
     */
    private String grainType;

    /**
     * 天气
     */
    private String weather;

    /**
     * 检测时间
     */
    private String checkTime;

    /**
     * 层数
     */
    private int floor;

    /**
     * 行数
     */
    private int row;

    /**
     * 列数
     */
    private int col;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGrainType() {
        return grainType;
    }

    public void setGrainType(String grainType) {
        this.grainType = grainType;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "Header{" +
                "title='" + title + '\'' +
                ", grainType='" + grainType + '\'' +
                ", weather='" + weather + '\'' +
                ", checkTime='" + checkTime + '\'' +
                '}';
    }
}
