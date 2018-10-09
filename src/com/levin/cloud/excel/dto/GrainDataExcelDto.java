package com.levin.cloud.excel.dto;


import com.levin.cloud.excel.entity.GrainDataEntity;

public class GrainDataExcelDto {
    /**
     * 头部数据
     */
    private Header header;

    /**
     * 传感器数据
     */
    private GrainDataEntity data;

    /**
     * 脚部数据
     */
    private Footer footer;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Footer getFooter() {
        return footer;
    }

    public void setFooter(Footer footer) {
        this.footer = footer;
    }

    public GrainDataEntity getData() {
        return data;
    }

    public void setData(GrainDataEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GrainDataExcelDto{" +
                "header=" + header +
                ", data=" + data +
                ", footer=" + footer +
                '}';
    }
}
