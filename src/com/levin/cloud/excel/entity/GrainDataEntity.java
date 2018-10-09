package com.levin.cloud.excel.entity;

import java.util.List;

/**
 * 整个敖间传感器数据
 *
 * @author lwt
 * @date 2018/3/5
 * @since 0.1.0
 */
public class GrainDataEntity {
    private List<FaceDataEntity> grainData;

    public List<FaceDataEntity> getGrainData() {
        return grainData;
    }

    public void setGrainData(List<FaceDataEntity> grainData) {
        this.grainData = grainData;
    }

    @Override
    public String toString() {
        return "GrainDataEntity{" +
                "grainData=" + grainData +
                '}';
    }

    /**
     * 将数据转换为数组便于画云图时调用
     *
     * @return
     */
    public double[][][] parseArray() {
        int floorNum = grainData.size();
        FaceDataEntity faceDataEntity = grainData.get(0);
        List<LineDataEntity> faceData = faceDataEntity.getFaceData();
        int rows = faceData.size();
        LineDataEntity lineDataEntity = faceData.get(0);
        int cols = lineDataEntity.getLineData().size();
        double[][][] result = new double[floorNum][rows][cols];
        for (int i = 0; i < floorNum; i++) {
            FaceDataEntity faceDataEntity1 = grainData.get(i);
            List<LineDataEntity> faceData1 = faceDataEntity1.getFaceData();
            for (int j = 0; j < rows; j++) {
                LineDataEntity lineDataEntity1 = faceData1.get(j);
                List<PointDataEntity> lineData = lineDataEntity1.getLineData();
                for (int k = 0; k < cols; k++) {
                    PointDataEntity pointDataEntity = lineData.get(k);
                    result[i][j][k] = pointDataEntity.getT();
                }
            }
        }
        return result;
    }
}
