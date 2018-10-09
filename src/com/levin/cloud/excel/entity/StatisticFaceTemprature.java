package com.levin.cloud.excel.entity;

/**
 * 各层温度统计值
 */
public class StatisticFaceTemprature {
    /**
     * 最高温度
     */
    private Double maxT;

    /**
     * 最低温度
     */
    private Double minT;

    /**
     * 平均温度
     */
    private Double T;

    public Double getMaxT() {
        return maxT;
    }

    public void setMaxT(Double maxT) {
        this.maxT = maxT;
    }

    public Double getMinT() {
        return minT;
    }

    public void setMinT(Double minT) {
        this.minT = minT;
    }

    public Double getT() {
        return T;
    }

    public void setT(Double t) {
        T = t;
    }
}
