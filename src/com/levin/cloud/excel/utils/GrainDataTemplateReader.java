package com.levin.cloud.excel.utils;

import com.levin.cloud.util.Json;
import com.levin.cloud.util.JsonUtils;
import com.levin.cloud.excel.dto.GrainDataExcelDto;
import com.levin.cloud.excel.dto.TitleDto;
import com.levin.cloud.excel.entity.*;
import com.levin.cloud.excel.imp.ExcelReader;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 读取粮情检测excel模板数据
 * 将excel分为三部分解析，不变的头部和尾部以及变化的数据区域
 * 返回结果为各个层数据的list,每个层包括行的list,每个行包括每个点的list
 *
 * @author lwt
 * @since 0.1.0
 * <p>
 * 注意：excel表格不能有合并或拆分的单元格，否者可能导致读取失败
 */
public class GrainDataTemplateReader {

    private static final String NUMBER_OF_NULL = "-9999";
    public static final Double DOUBLE_OF_NULL = -9999D;

    /**
     * 读取Excel粮情数据(仅支持单个sheet)
     *
     * @param wb excel文件
     * @return
     * @throws IOException
     */
    private static GrainDataExcelDto read(Workbook wb) {
        if (wb == null) {
            return null;
        }

        GrainDataExcelDto grainDataExcelDto = new GrainDataExcelDto();
        GrainDataEntity grainDataEntity = new GrainDataEntity();
        com.levin.cloud.excel.dto.Header header = new com.levin.cloud.excel.dto.Header();

        //解析标题和头部数据
        Sheet sheet = wb.getSheetAt(0);
        TitleDto titleDto = getTitle(sheet);
        header.setTitle(titleDto.getTitle());
        header = getHeader(sheet, header);
        grainDataExcelDto.setHeader(header);

        int p = 4;
        int m = titleDto.getFloorNum();
        int n = titleDto.getLineNum();
        int k = titleDto.getColumeNum();

        header.setFloor(m);
        header.setRow(n);
        header.setCol(k);

        //解析数据
        List<FaceDataEntity> faceDataEntities = new ArrayList<>();
        for (int i = 0; i < m; i++) {//循环层
            FaceDataEntity faceDataEntity = new FaceDataEntity();
            List<LineDataEntity> lineDataEntities = new ArrayList<>();
            for (int j = 0; j < 2 * n; j += 2) {//循环行
                LineDataEntity lineDataEntity = new LineDataEntity();
                List<PointDataEntity> pointDataEntities = new ArrayList<>();
                Row row1 = sheet.getRow(p + i * n * 2 + j);
                Row row2 = sheet.getRow(p + i * n * 2 + j + 1);
                //System.out.println(p + i * n + j + i * n);

                for (int c = 1; c <= 2 * k; c += 2) {//循环列
                    //System.out.print(i + "_" + j + "_" + c + "   ");
                    PointDataEntity pointDataEntity = new PointDataEntity();
                    Cell tCell = row1.getCell(c);
                    Cell hCell = row1.getCell(c + 1);
                    Cell dCell = row2.getCell(c);
                    Cell gCell = row2.getCell(c + 1);

                    double t = tCell == null ? Double.MAX_VALUE : getFotmatDobleValue(tCell);
                    double h = hCell == null ? Double.MAX_VALUE : getFotmatDobleValue(hCell);
                    double d = dCell == null ? Double.MAX_VALUE : getFotmatDobleValue(dCell);
                    double g = gCell == null ? Double.MAX_VALUE : getFotmatDobleValue(gCell);

                    pointDataEntity.setT(t);  //温度
                    pointDataEntity.setH(h);  //湿度
                    pointDataEntity.setD(d);  //露点
                    pointDataEntity.setG(g);  //气体条件
                    pointDataEntity.setX(j / 2 + 1);
                    pointDataEntity.setY((c - 1) / 2 + 1);
                    pointDataEntity.setZ(i + 1);
                    pointDataEntities.add(pointDataEntity);
                }
                //System.out.println();
                lineDataEntity.setLineData(pointDataEntities);
                lineDataEntities.add(lineDataEntity);
            }
            faceDataEntity.setFaceData(lineDataEntities);
            faceDataEntities.add(faceDataEntity);
        }

        //解析脚部数据
        grainDataEntity.setGrainData(faceDataEntities);
        grainDataExcelDto.setData(grainDataEntity);
        com.levin.cloud.excel.dto.Footer footer = getFooter(sheet, p + m * n * 2, m);
        grainDataExcelDto.setFooter(footer);
        return grainDataExcelDto;
    }

    /**
     * 解析标题
     *
     * @param sheet sheet
     * @return
     */
    private static TitleDto getTitle(Sheet sheet) {
        Row titleRow = sheet.getRow(0);
        Cell titleCell = titleRow.getCell(0);
        String title = titleCell.getStringCellValue();
        String[] strs = title.split("_");

        TitleDto titleDto = new TitleDto();
        titleDto.setTitle(strs[0]);
        titleDto.setLineNum(Integer.valueOf(strs[1]));
        titleDto.setColumeNum(Integer.valueOf(strs[2]));
        titleDto.setFloorNum(Integer.valueOf(strs[3]));

        return titleDto;
    }

    /**
     * 解析头部数据
     *
     * @param sheet
     * @param header
     * @return
     */
    private static com.levin.cloud.excel.dto.Header getHeader(Sheet sheet, com.levin.cloud.excel.dto.Header header) {
        Row row = sheet.getRow(1);
        Cell cell = row.getCell(1);
        header.setGrainType(cell.getStringCellValue());
        Cell cell1 = row.getCell(3);
        header.setWeather(cell1.getStringCellValue());
        Cell cell2 = row.getCell(5);
        header.setCheckTime(getCellFormatValue(cell2));
        return header;
    }


    /**
     * 解析脚部数据
     *
     * @param sheet
     * @param p
     * @return
     */
    private static com.levin.cloud.excel.dto.Footer getFooter(Sheet sheet, int p, int m) {
        com.levin.cloud.excel.dto.Footer footer = new com.levin.cloud.excel.dto.Footer();
        Row row = sheet.getRow(p);
        footer.setCT(getFotmatDobleValue(row.getCell(1)));
        footer.setCH(getFotmatDobleValue(row.getCell(3)));
        footer.setGT(getFotmatDobleValue(row.getCell(5)));
        footer.setGH(getFotmatDobleValue(row.getCell(7)));

        Row row1 = sheet.getRow(p + 4);
        List<StatisticFaceTemprature> sfts = new ArrayList<>();
        for (int i = 0; i <= m; i++) {
            StatisticFaceTemprature sft = new StatisticFaceTemprature();
            sft.setMaxT(getFotmatDobleValue(row1.getCell(i * 3)));
            sft.setMinT(getFotmatDobleValue(row1.getCell(i * 3 + 1)));
            sft.setT(getFotmatDobleValue(row1.getCell(i * 3 + 2)));
            sfts.add(sft);
        }
        footer.setSft(sfts);

        Row row2 = sheet.getRow(p + 5);
        footer.setProduceYear(String.valueOf(getCellFormatValue(row2.getCell(1))));
        footer.setNumber(row2.getCell(3).getNumericCellValue());
        footer.setSize(row2.getCell(5).getStringCellValue());

        Row row3 = sheet.getRow(p + 6);
        footer.setInHumity(Double.valueOf(getCellFormatValue(row3.getCell(1))));
        footer.setHumity(row3.getCell(3).getNumericCellValue());
        footer.setPlace(row3.getCell(5).getStringCellValue());

        Row row4 = sheet.getRow(p + 7);
        footer.setColorSmell(row4.getCell(1).getStringCellValue());
        footer.setWaterUptake(Double.valueOf(getCellFormatValue(row4.getCell(3))));

        Row row5 = sheet.getRow(p + 8);
        footer.setFattyAcid(Double.valueOf(getCellFormatValue(row5.getCell(1))));
        footer.setTasteScore(Double.valueOf(getCellFormatValue(row5.getCell(3))));

        Row row6 = sheet.getRow(p + 9);
        footer.setInTime(getCellFormatValue(row6.getCell(1)));

        Row row7 = sheet.getRow(p + 10);
        footer.setCheckName(row7.getCell(1).getStringCellValue());
        footer.setCustodian(row7.getCell(3).getStringCellValue());

        return footer;
    }


    /**
     * Excel处理2003与2007差异
     */
    public static Workbook getWorkBook(File excel) throws IOException {
        return excel.getName().endsWith("xls") ?
                new HSSFWorkbook(new BufferedInputStream(new FileInputStream(excel))) :
                excel.getName().endsWith("xlsx") ?
                        new XSSFWorkbook(new BufferedInputStream(new FileInputStream(excel))) : null;
    }

    private static Workbook getWorkBook(InputStream is) throws IOException {
        return new HSSFWorkbook(new BufferedInputStream(is));
    }

    /**
     * 检查excel文件格式
     *
     * @param wb excel文件
     * @return
     * @throws IOException
     */
    private static Json check(Workbook wb) throws IOException {
        Sheet sheet = wb.getSheetAt(0);
        boolean flag = true;

        //错误格式
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.RED.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


        //检查title格式
        Row titleRow = sheet.getRow(0);
        Cell titleCell = titleRow.getCell(0);
        if (titleCell == null) {
            Cell cell = titleRow.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("标题不能为空");
            return new Json().setSuccess(true).setData(wb);
        }
        String title = titleCell.getStringCellValue();
        String[] strs = title.split("_");
        if (!(strs.length == 4)) {
            titleCell.setCellStyle(style);
            titleCell.setCellValue("标题格式错误");
            return new Json().setSuccess(false).setData(wb);
        }

        int m = Integer.valueOf(strs[1]);
        int n = Integer.valueOf(strs[2]);
        int k = Integer.valueOf(strs[3]);

        //将excel文件内容与模板中的内容对比
        GrainDataTemplate gdt = new GrainDataTemplate(m, n, k);
        Object[][] template = gdt.getTemplate();
        Object[][] content = getContent(sheet);

        if (template.length != content.length /*|| template[0].length != content[0].length*/) {
            return new Json().setSuccess(false).setData(wb);
        }

        for (int i = 1; i < template.length; i++) {
            for (int j = 0; j < template[1].length; j++) {
                if (template[i][j] == null) {
                    continue;
                }

                Object o = content[i][j];
                Object t = template[i][j];

                //非空判断
                if (t.equals(GrainDataTemplate.FLAG)) {
                    /*if (o == null || StringUtils.isEmptyString(o.toString())) {
                        flag = false;
                        Cell cell = sheet.getRow(i).getCell(j);
                        cell.setCellStyle(style);
                    }*/
                } else {
                    //相等判断
                    if (o == null || !o.toString().equals(t.toString())) {
                        flag = false;
                        Cell cell = sheet.getRow(i).getCell(j);
                        cell.setCellStyle(style);
                    }
                }

            }
        }


        Json json = new Json();
        if (flag) {
            json.setSuccess(true);
            json.setData(read(wb));
        } else {
            json.setSuccess(false);
            json.setData(wb);
        }
        return json;
    }

    /**
     * 检查excel格式
     *
     * @param is 输入流
     * @return
     * @throws IOException
     */
    public static Json check(InputStream is) throws IOException {
        return check(getWorkBook(is));
    }

    public static Json check(File excel) throws IOException {
        Workbook wb = getWorkBook(excel);
        if (wb == null) {
            return null;
        }
        return check(wb);
    }

    /**
     * 以数组的方式获取excel文件内容
     *
     * @param sheet 工作簿
     * @return
     */
    private static Object[][] getContent(Sheet sheet) {
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        Object[][] content = new Object[rowNum + 1][colNum];
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                Cell cell = row.getCell((short) j);
                content[i][j] = ExcelReader.getStringCellValue(cell).trim();
                j++;
            }
        }

        return content;
    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell1
     * @return
     */
    public static String getCellFormatValue(Cell cell1) {
        try {
            HSSFCell cell = (HSSFCell) cell1;
            String cellvalue;
            if (cell != null) {
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                    case HSSFCell.CELL_TYPE_FORMULA: {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            cellvalue = sdf.format(date);

                        }
                        // 如果是纯数字
                        else {
                            // 取得当前Cell的数值
                            cellvalue = String.valueOf(cell.getNumericCellValue());
                        }
                        break;
                    }
                    // 如果当前Cell的Type为STRIN
                    case HSSFCell.CELL_TYPE_STRING:
                        // 取得当前的Cell字符串
                        cellvalue = cell.getRichStringCellValue().getString();
                        break;
                    // 默认的Cell值
                    default:
                        cellvalue = NUMBER_OF_NULL;
                }
            } else {
                cellvalue = NUMBER_OF_NULL;
            }
            return cellvalue;
        } catch (Exception e) {

        }

        return NUMBER_OF_NULL;


    }

    public static double fomatString(String value) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.valueOf(df.format(Double.valueOf(value)));
    }

    public static double getFotmatDobleValue(Cell cell) {
        return fomatString(getCellFormatValue(cell));
    }


    @Test
    public void test() throws IOException {
        String file = "D:/G003.xls";
        File excel = new File(file);
        Json check = check(excel);
        if (!check.isSuccess()) {
            Workbook wb = (Workbook) check.getData();
            OutputStream os = new FileOutputStream(new File("D:\\false.xls"));
            wb.write(os);
        } else {
            GrainDataExcelDto grainDataExcelDto = (GrainDataExcelDto) check.getData();
            System.out.println(JsonUtils.toJson(grainDataExcelDto));
            double[][][] data = grainDataExcelDto.getData().parseArray();
            System.out.println(JsonUtils.toJson(data));
        }
    }

    @Test
    public void test2() {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        System.out.println(Double.valueOf(df.format(-9999)));
    }


}
