package com.levin.cloud.excel.dto;


import com.levin.cloud.excel.entity.StatisticFaceTemprature;

import java.util.List;

/**
 * @author Li wantao
 */
public class Footer {
    /**
     * 仓温
     */
    private Double CT;

    /**
     * 仓湿
     */
    private Double CH;

    /**
     * 气温
     */
    private Double GT;

    /**
     * 气湿
     */
    private Double GH;

    /**
     * 各层温度统计值
     */
    private List<StatisticFaceTemprature> sft;

    /**
     * 生产年份
     */
    private String produceYear;

    /**
     * 数量
     */
    private Double number;

    /**
     * 粮堆尺寸
     */
    private String size;

    /**
     * 入仓水分
     */
    private Double inHumity;

    /**
     * 当前水分
     */
    private Double humity;

    /**
     * 产地
     */
    private String place;

    /**
     * 色泽气味
     */
    private String colorSmell;

    /**
     * 面筋吸水量
     */
    private Double waterUptake;

    /**
     * 脂肪酸值
     */
    private Double fattyAcid;

    /**
     * 品尝评分值
     */
    private Double tasteScore;

    /**
     * 入仓时间
     */
    private String inTime;

    /**
     * 检测人
     */
    private String checkName;

    /**
     * 保管员
     */
    private String custodian;

    public Double getCT() {
        return CT;
    }

    public void setCT(Double CT) {
        this.CT = CT;
    }

    public Double getCH() {
        return CH;
    }

    public void setCH(Double CH) {
        this.CH = CH;
    }

    public Double getGT() {
        return GT;
    }

    public void setGT(Double GT) {
        this.GT = GT;
    }

    public Double getGH() {
        return GH;
    }

    public void setGH(Double GH) {
        this.GH = GH;
    }

    public List<StatisticFaceTemprature> getSft() {
        return sft;
    }

    public void setSft(List<StatisticFaceTemprature> sft) {
        this.sft = sft;
    }

    public String getProduceYear() {
        return produceYear;
    }

    public void setProduceYear(String produceYear) {
        this.produceYear = produceYear;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getInHumity() {
        return inHumity;
    }

    public void setInHumity(Double inHumity) {
        this.inHumity = inHumity;
    }

    public Double getHumity() {
        return humity;
    }

    public void setHumity(Double humity) {
        this.humity = humity;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getColorSmell() {
        return colorSmell;
    }

    public void setColorSmell(String colorSmell) {
        this.colorSmell = colorSmell;
    }

    public Double getWaterUptake() {
        return waterUptake;
    }

    public void setWaterUptake(Double waterUptake) {
        this.waterUptake = waterUptake;
    }

    public Double getFattyAcid() {
        return fattyAcid;
    }

    public void setFattyAcid(Double fattyAcid) {
        this.fattyAcid = fattyAcid;
    }

    public Double getTasteScore() {
        return tasteScore;
    }

    public void setTasteScore(Double tasteScore) {
        this.tasteScore = tasteScore;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getCustodian() {
        return custodian;
    }

    public void setCustodian(String custodian) {
        this.custodian = custodian;
    }

    @Override
    public String toString() {
        return "Footer{" +
                "CT=" + CT +
                ", CH=" + CH +
                ", GT=" + GT +
                ", GH=" + GH +
                ", sft=" + sft +
                ", produceYear='" + produceYear + '\'' +
                ", number=" + number +
                ", size='" + size + '\'' +
                ", inHumity=" + inHumity +
                ", humity=" + humity +
                ", place='" + place + '\'' +
                ", colorSmell='" + colorSmell + '\'' +
                ", waterUptake=" + waterUptake +
                ", fattyAcid=" + fattyAcid +
                ", tasteScore=" + tasteScore +
                ", inTime='" + inTime + '\'' +
                ", checkName='" + checkName + '\'' +
                ", custodian='" + custodian + '\'' +
                '}';
    }
}
