package com.levin.cloud.excel.imp;


import com.levin.cloud.excel.utils.GrainDataTemplateReader;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取Excel数据工具类
 * @time 2017-06-03
 */
public class ExcelReader {
    private POIFSFileSystem fs;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFRow row;

    /**
     * 获取多个sheetExcel表格数据
     *
     * @param is Excel 数据表格
     * @return
     */
    public Map<Integer, Object> readMultiSheetExcel(InputStream is) {
        Map<Integer, Object> excelDatas = new HashMap<>();
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Integer sheetNum = wb.getNumberOfSheets();
        //循环获取所有sheet数据
        for (int i = 0; i < sheetNum; i++) {
            Map<String,Object> excelData = new HashMap<>();
            HSSFSheet sheet = wb.getSheetAt(i);
            excelData.put("title",readExcelTitleBySheet(sheet));
            excelData.put("content",readExcelContentBySheet(sheet));
            excelDatas.put(i,excelData);
        }
        return excelDatas;
    }

    /**
     * 获取单个Sheet Excel表格表头内容
     *
     * @param sheet excel工作簿
     * @return
     */
    public String[] readExcelTitleBySheet(HSSFSheet sheet) {
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }

    public Map<Integer,Object> readExcelContentBySheet(HSSFSheet sheet){
        Map<Integer, Object> content = new HashMap<Integer, Object>();
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题

        for (int i = 1; i <= rowNum; i++) {
            int j = 0;
            row = sheet.getRow(i);
            Map<Integer, String> rowContent = new HashMap<>();
            while (j < colNum) {
                rowContent.put(j, getStringCellValue(row.getCell((short) j)).trim());
                j++;
            }
            content.put(i, rowContent);
        }

        return content;
    }

    /**
     * 读取Excel表格表头的内容
     *
     * @param is 输入流
     * @return String[] 表头内容的数组
     */
    public String[] readExcelTitle(InputStream is) {
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            //title[i] = getStringCellValue(row.getCell((short) i));
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     *
     * @param is 输入流
     * @return Map 包含单元格数据内容的Map对象
     */
    public Map<Integer, Object> readExcelContent(InputStream is) {
        Map<Integer, Object> content = new HashMap<Integer, Object>();
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题

        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, String> rowContent = new HashMap<>();
            while (j < colNum) {

                rowContent.put(j, getStringCellValue(row.getCell((short) j)).trim());
                j++;
            }
            content.put(i, rowContent);
        }

        return content;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    public static String getStringCellValue(Cell cell) {
        String strCell;
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(new BigDecimal(cell.getNumericCellValue()).toPlainString());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getDateCellValue(HSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    private String getCellFormatValue(HSSFCell cell) {
        return GrainDataTemplateReader.getCellFormatValue(cell);
    }
}
