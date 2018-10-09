package com.levin.cloud;


import com.levin.cloud.dto.TempSecDto;
import com.levin.cloud.dto.CloudChartDto;
import com.levin.cloud.util.FileUtils;
import com.levin.cloud.excel.dto.GrainDataExcelDto;
import com.levin.cloud.excel.utils.GrainDataTemplateReader;
import com.levin.cloud.util.Json;
import org.apache.poi.ss.usermodel.Workbook;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * 云图绘制工具类
 *
 * @author Levin
 * @date 2018/03/20
 * @since 1.0.0
 */

public class CloudChart {


    /**
     * 生成云图
     *
     * @param cloudChartDto 输入参数
     * @param ccp           绘图参数
     * @param data          数据
     * @param rowNum        插值行数
     * @param colNum        插值列数
     * @param num           插值参数
     */
    public static DrawingPanel draw(CloudChartDto cloudChartDto, CloudChartPara ccp, double[][] data, int rowNum, int colNum, int num, double[] TEMPLATE_VALUES) {
        //计算传感器之间的真实距离
        double lengthx = (cloudChartDto.getLengthx() - 1) / (cloudChartDto.getXnum() - 1);
        double lengthy = (cloudChartDto.getLengthy() - 1) / (cloudChartDto.getYnum() - 1);

        //初始化drawPanel
        DrawingPanel dp = new DrawingPanel();
        dp.ClearObjects();

        //设置参数
        dp.setSize(ccp.getLength(), ccp.getWidth());
        dp.setStartColor(ccp.getStart());
        dp.setEndColor(ccp.getEnd());
        dp.setDrawGridData(ccp.is_drawGridData());
        dp.setDrawBorderLine(ccp.is_drawBorderLine());
        dp.setDrawContourLine(ccp.is_drawContourLine());
        dp.setDrawDiscreteData(ccp.is_drawDiscreteData());
        dp.set_drawData(ccp.is_drawData());
        dp.setDrawTitle(ccp.isDrawTitle());
        dp.setTitle(cloudChartDto.buildTitle());
        dp.setCurrentFloor(cloudChartDto.getFloorNum());
        dp.setFloorNum(cloudChartDto.getTotalFloor());
        dp.setCheckTime(cloudChartDto.getTime());
        dp.setAbscissa(ccp.getAbscissa());
        dp.setOrdinate(ccp.getOrdinate());
        dp.set_drawContourValue(ccp.is_drawContourValue());
        dp.setPlane_type(ccp.getPlane_type());
        dp.setRoom_type(ccp.getRoom_type());

        //输入数据并生成网格数据（插值）
        dp.addData(data, lengthx, lengthy);
        dp.addGridData(rowNum, colNum, num, cloudChartDto.getLengthx(), cloudChartDto.getLengthy());

        //生成云图
        dp.setLegendVavues(TEMPLATE_VALUES);   //设置图例范围
        dp.setPrecision(ccp.getPrecision());   //设置画图精度
        dp.TracingContourLines();              //追踪等值线
        dp.SmoothLines();                      //平滑等值线
        dp.TracingPolygons();                  //追踪多边形
        dp.ClipPolygons();                     //剪辑多边形
        dp.SetCoordinate(0, cloudChartDto.getLengthx(), 0, cloudChartDto.getLengthy());   //设置坐标范围
        dp.CreateLegend();                     //创建图例
        dp.createPointTemp();
        dp.findPolygonHighValue();
        dp.repaint();

        return dp;
    }

    /**
     * 保存图片到指定文件
     */
    public static boolean savePic(DrawingPanel dp, String path) {
        Dimension imageSize = dp.getSize();
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        dp.paint(graphics);
        graphics.dispose();
        try {
            File file = new File(path);
            if (!FileUtils.checkFileExists(file.getPath())) {
                FileUtils.createDir(file.getParent());
            }

            return ImageIO.write(image, "png", new File(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 以流的方式生成云图
     */
    public static void drawToStream(CloudChartDto ccd, CloudChartPara ccp, double[][] data, int rowNum, int colNum, int num, double[] TEMPLATE_VALUES, OutputStream os) {
        DrawingPanel dp = draw(ccd, ccp, data, rowNum, colNum, num, TEMPLATE_VALUES);
        Dimension imageSize = dp.getSize();
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        dp.paint(graphics);
        graphics.dispose();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 以流的方式生成云图
     *
     * @param dp dp
     * @param os os
     */
    public static void drawToStream(DrawingPanel dp, OutputStream os) {
        Dimension imageSize = dp.getSize();
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        dp.paint(graphics);
        graphics.dispose();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 以文件的方式生成云图
     */
    public static String drawToFile(CloudChartDto ccd, CloudChartPara ccp, double[][] data, int rowNum, int colNum, int num, double[] TEMPLATE_VALUES) {
        String path = ccd.getPath();
        DrawingPanel dp = draw(ccd, ccp, data, rowNum, colNum, num, TEMPLATE_VALUES);
        savePic(dp, path);

        return path;
    }

    public static boolean drawToFile(DrawingPanel dp, String path) {
        return savePic(dp, path);
    }


    public static void main(String[] args) throws IOException {
        int type = 2;
        //String file = "D:/model1.xls";
        String file = "D:/P38.xls";
        File excel = new File(file);
        Json check = GrainDataTemplateReader.check(excel);
        if (!check.isSuccess()) {
            Workbook wb = (Workbook) check.getData();
            OutputStream os = new FileOutputStream(new File("D:\\false.xls"));
            wb.write(os);
            System.out.println("error");
        } else {
            GrainDataExcelDto grainDataExcelDto = (GrainDataExcelDto) check.getData();
            //System.out.println(JsonUtils.toJson(grainDataExcelDto));
            double[][][] datas1 = grainDataExcelDto.getData().parseArray();
            double[][][] datas = parse(datas1, type);
            //System.out.println(datas);

            int i = 1;
            int f = datas.length;
            int r = datas[0].length;
            int c = datas[0][0].length;
            //System.out.println(f + "," + r + "," + c);
            for (double[][] data : datas) {
                CloudChartDto ccd = new CloudChartDto();
                ccd.setPath("D:\\cloud.png");
                //ccd.setLengthx(3.0 * r);
                ccd.setLengthx(3.0 * c);
                ccd.setLengthy(3.0 * r);
                ccd.setXnum(c);
                ccd.setYnum(r);
                ccd.setName("凌海库");
                ccd.setTime("2018-04-10");
                ccd.setType("水平");
                ccd.setFloorNum(i);
                ccd.setTotalFloor(f);

                double[] TEMPLATE_VALUES = new double[]{-10, -5, 0, 5, 10, 15, 20, 25, 30, 35};
                CloudChartPara ccp = new CloudChartPara();
                ccp.set_drawContourLine(false);
                ccp.set_drawContourValue(true);
                ccp.setRoom_type(DrawingPanel.ROOM_TYPE.BUNGALOW);
                ccp.setPrecision(0.5);
                ccp.set_drawContourValue(true);

                //ccp.setWidth(1000);
                //ccp.setLength(1000);
                if (type == 0)
                    ccp.setPlane_type(DrawingPanel.PLANE_TYPE.OVER);
                if (type == 1)
                    ccp.setPlane_type(DrawingPanel.PLANE_TYPE.FRONT);
                if (type == 2)
                    ccp.setPlane_type(DrawingPanel.PLANE_TYPE.VISION);
                DrawingPanel dp = draw(ccd, ccp, data, 100, 100, 8, TEMPLATE_VALUES);
                //drawToStream(dp,os);
                //dp.setRoom_type(DrawingPanel.ROOM_TYPE.CYLINDER);
                java.util.List<TempSecDto> tempSecDtoList = new ArrayList<>();
                TempSecDto tempSecDto = new TempSecDto(5, 10, Color.RED);
                tempSecDtoList.add(tempSecDto);
                //dp.mergeColor(tempSecDtoList,0);
                File pic = new File("D:\\cloud\\" + type + "\\cloud" + i + ".png");
                if (!FileUtils.checkFileExists(pic.getPath())) {
                    FileUtils.createDir(pic.getParent());
                }
                OutputStream os = new FileOutputStream(pic);
                drawToStream(dp, os);
                i++;
            }
        }


    }

    private static double[][][] parse(double[][][] data, int type) {
        int floor = data.length;
        int row = data[0].length;
        int col = data[0][0].length;
        double[][][] result;
        if (type == 1) {
            result = new double[row][floor][col];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < floor; j++) {
                    for (int k = 0; k < col; k++) {
                        result[i][j][k] = data[j][i][k];
                    }
                }
            }
        } else if (type == 2) {
            result = new double[col][floor][row];
            for (int i = 0; i < col; i++) {
                for (int j = 0; j < floor; j++) {
                    for (int k = 0; k < row; k++) {
                        result[i][j][k] = data[j][k][i];
                    }
                }
            }
        } else {
            result = data;
        }

        return result;
    }


}
