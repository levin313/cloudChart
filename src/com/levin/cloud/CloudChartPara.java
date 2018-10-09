package com.levin.cloud;

import java.awt.*;

public class CloudChartPara {
    /**
     * 起始颜色
     */
    private Color start = new Color(0, 0, 0);
    /**
     * 终止颜色
     */
    private Color end = new Color(255, 255, 255);

    /**
     * 是否画插值点
     */

    private boolean _drawGridData = false;

    /**
     * 是否画边界线
     */
    private boolean _drawBorderLine = true;
    /**
     * 是否画等值线
     */
    private boolean _drawContourLine = false;
    /**
     * 是否填充
     */
    private boolean _drawContourPolygon = true;
    /**
     * 是否画离散点
     */
    private boolean _drawDiscreteData = true;

    /**
     * 是否画温度统计值
     */
    private boolean _drawData = true;

    /**
     * 画图精度
     */
    private double precision = 0.5;

    /**
     * 画布长度
     */
    private int length = 1600;

    /**
     * 画布宽度
     */
    private int width = 900;

    private boolean drawTitle = true;

    private String abscissa = "由西向东（单位:米）";
    private String ordinate = "由南向北（单位:米）";
    private boolean _drawContourValue = true;
    private DrawingPanel.PLANE_TYPE plane_type = DrawingPanel.PLANE_TYPE.OVER;
    private DrawingPanel.ROOM_TYPE room_type = DrawingPanel.ROOM_TYPE.BUNGALOW;

    public String getAbscissa() {
        return abscissa;
    }

    public void setAbscissa(String abscissa) {
        this.abscissa = abscissa;
    }

    public String getOrdinate() {
        return ordinate;
    }

    public void setOrdinate(String ordinate) {
        this.ordinate = ordinate;
    }

    public Color getStart() {
        return start;
    }

    public void setStart(Color start) {
        this.start = start;
    }

    public Color getEnd() {
        return end;
    }

    public void setEnd(Color end) {
        this.end = end;
    }

    public boolean is_drawGridData() {
        return _drawGridData;
    }

    public void set_drawGridData(boolean _drawGridData) {
        this._drawGridData = _drawGridData;
    }

    public boolean is_drawBorderLine() {
        return _drawBorderLine;
    }

    public void set_drawBorderLine(boolean _drawBorderLine) {
        this._drawBorderLine = _drawBorderLine;
    }

    public boolean is_drawContourLine() {
        return _drawContourLine;
    }

    public void set_drawContourLine(boolean _drawContourLine) {
        this._drawContourLine = _drawContourLine;
    }

    public boolean is_drawContourPolygon() {
        return _drawContourPolygon;
    }

    public void set_drawContourPolygon(boolean _drawContourPolygon) {
        this._drawContourPolygon = _drawContourPolygon;
    }


    public boolean is_drawDiscreteData() {
        return _drawDiscreteData;
    }

    public void set_drawDiscreteData(boolean _drawDiscreteData) {
        this._drawDiscreteData = _drawDiscreteData;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean is_drawData() {
        return _drawData;
    }

    public void set_drawData(boolean _drawData) {
        this._drawData = _drawData;
    }

    public boolean isDrawTitle() {
        return drawTitle;
    }

    public void setDrawTitle(boolean drawTitle) {
        this.drawTitle = drawTitle;
    }

    public boolean is_drawContourValue( ) {
        return _drawContourValue;
    }

    public void set_drawContourValue(boolean _drawContourValue) {
        this._drawContourValue = _drawContourValue;
    }

    public DrawingPanel.PLANE_TYPE getPlane_type() {
        return plane_type;
    }

    public void setPlane_type(DrawingPanel.PLANE_TYPE plane_type) {
        this.plane_type = plane_type;
    }

    public DrawingPanel.ROOM_TYPE getRoom_type() {
        return room_type;
    }

    public void setRoom_type(DrawingPanel.ROOM_TYPE room_type) {
        this.room_type = room_type;
    }
}
