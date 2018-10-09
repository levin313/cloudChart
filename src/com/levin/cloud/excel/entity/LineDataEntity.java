package com.levin.cloud.excel.entity;

import java.util.List;

/**
 * @author lwt
 * @date 2018/3/5
 * @since 0.1.0
 */
public class LineDataEntity {
    /**
     * 某一行/列的传感器数据
     */
    private List<PointDataEntity> lineData;

    public List<PointDataEntity> getLineData() {
        return lineData;
    }

    public void setLineData(List<PointDataEntity> lineData) {
        this.lineData = lineData;
    }

    @Override
    public String toString() {
        return "LineDataEntity{" +
                "lineData=" + lineData +
                '}';
    }
}
