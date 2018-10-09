package com.levin.cloud.excel.dto;


public class TitleDto {
    /**
     * 标题
     */
    private String title;

    /**
     * （传感器）行数
     */
    private int lineNum;

    /**
     * 列数
     */
    private int columeNum;

    /**
     * 层数
     */
    private int floorNum;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getColumeNum() {
        return columeNum;
    }

    public void setColumeNum(int columeNum) {
        this.columeNum = columeNum;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }
}
