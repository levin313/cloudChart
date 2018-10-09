package com.levin.cloud.excel.entity;

import java.util.List;

/**
 * 某一层传感器数据
 *
 * @author lwt
 * @date 2018/3/5
 * @since 0.1.0
 */
public class FaceDataEntity {
    /**
     * 某一截面传感器数据(一层)
     */
    private List<LineDataEntity> faceData;

    public List<LineDataEntity> getFaceData() {
        return faceData;
    }

    public void setFaceData(List<LineDataEntity> faceData) {
        this.faceData = faceData;
    }

    @Override
    public String toString() {
        return "FaceDataEntity{" +
                "faceData=" + faceData +
                '}';
    }
}
