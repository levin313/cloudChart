package com.levin.cloud.excel.utils;

import java.util.Arrays;
import java.util.List;

/**
 * 粮情检测数据excel模板
 * 根据此模板（哪些地方的数据是固定的，哪些地方的数据是非空的...）对上传的excel模板进行格式检查
 *
 * @author Levin
 * @date 2018-04-16
 */
public class GrainDataTemplate {
    /**
     * 行数
     */
    private Integer row;
    /**
     * 列数
     */
    private Integer col;
    /**
     * 层数
     */
    private Integer floor;
    /**
     * 生成的模板：以二维数组模拟excel表格
     */
    private Object[][] template;

    private static List<String> header = Arrays.asList("品种", "天气", "检测时间");
    private static List<String> midder = Arrays.asList("仓温", "仓湿", "气温", "气湿");
    private static List<String> footer = Arrays.asList("生产年份", "数量", "粮堆尺寸", "入仓水分", "当前水分", "产地", "色泽气味", "面筋吸水量", "脂肪酸值", "品尝评分值", "入仓时间", "检测人", "保管员");
    public static String FLAG = "非空";


    public GrainDataTemplate(Integer m, Integer n, Integer k) {
        this.row = m;
        this.col = n;
        this.floor = k;

        createTemplate();
    }

    private void createTemplate() {
        int maxCol1 = 3 * (this.floor + 1);
        int maxCol2 = this.col * 2 + 2;
        this.template = new Object[this.row * this.floor * 2 + 15][ Math.max(maxCol1,maxCol2)];
        template[0][0] = FLAG;
        for (int i = 0; i < header.size(); i++) {
            template[1][i * 2] = header.get(i);
            template[1][i * 2 + 1] = FLAG;
        }
        int p = row * floor * 2 + 4;
        for (int i = 0; i < midder.size(); i++) {
            template[p][i * 2] = midder.get(i);
            template[p][i * 2 + 1] = FLAG;
        }

        p = p + 3;
        for (int i = 0; i <= floor; i++) {
            template[p][3 * i] = "最高";
            template[p][3 * i + 1] = "最低";
            template[p][3 * i + 2] = "平均";
        }

        p = p + 2;
        for (int i = 0; i < footer.size(); i++) {
            if (i < 3) {
                template[p][i * 2] = footer.get(i);
                template[p][i * 2 + 1] = FLAG;
            }
            if (i >= 3 && i < 6) {
                template[p + 1][(i - 3) * 2] = footer.get(i);
                template[p + 1][(i - 3) * 2 + 1] = FLAG;
            }
            if (i >= 6 && i < 8) {
                template[p + 2][(i - 6) * 2] = footer.get(i);
                template[p + 2][(i - 6) * 2 + 1] = FLAG;
            }
            if (i >= 8 && i < 10) {
                template[p + 3][(i - 8) * 2] = footer.get(i);
                template[p + 3][(i - 8) * 2 + 1] = FLAG;
            }
            if (i == 10) {
                template[p + 4][0] = footer.get(i);
                template[p + 4][1] = FLAG;
            }
            if (i >= 11 && i < 13) {
                template[p + 5][(i - 11) * 2] = footer.get(i);
                template[p + 5][(i - 11) * 2 + 1] = FLAG;
            }

        }
    }


    public Object[][] getTemplate() {
        return template;
    }

}
