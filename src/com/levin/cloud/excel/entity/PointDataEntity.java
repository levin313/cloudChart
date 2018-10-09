package com.levin.cloud.excel.entity;

/**
 * 单一传感器点的粮情数据
 *
 * @author lwt
 * @date 2018/3/5
 * @since 0.1.0
 */
public class PointDataEntity {
    /**
     * 温度
     */
    private Double T;

    /**
     * 湿度
     */
    private Double H;

    /**
     * 露点
     */
    private Double D;

    /**
     * 水分
     */
    private Double G;

    /**
     * xyz为传感器编号
     */
    private int x;
    private int y;
    private int z;

    public Double getT() {
        return T;
    }

    public void setT(Double t) {
        T = t;
    }

    public Double getH() {
        return H;
    }

    public void setH(Double h) {
        H = h;
    }

    public Double getD() {
        return D;
    }

    public void setD(Double d) {
        D = d;
    }

    public Double getG() {
        return G;
    }

    public void setG(Double g) {
        G = g;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "PointDataEntity{" +
                "T=" + T +
                ", H=" + H +
                ", D=" + D +
                ", G=" + G +
                '}';
    }
}
