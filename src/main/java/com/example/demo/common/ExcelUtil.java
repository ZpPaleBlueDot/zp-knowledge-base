package com.example.demo.common;

import org.apache.poi.common.Duplicatable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {

    /**
     * #合并多个excel文件
     *
     * @param fileLists excel文件路径
     * @param path      目标文件保存目录
     * @param fileName  目标文件名称
     */
    public static void mergeExcel(List<String> fileLists, String path, String fileName) {
        // 创建新的excel工作簿
        XSSFWorkbook newExcelWorkBook = new XSSFWorkbook();
        // 遍历需要合并的excel文件
        for (String excelName : fileLists) {
            try (InputStream in = new FileInputStream(excelName)) {
                // 创建工作簿
                XSSFWorkbook tmpWorkBook = new XSSFWorkbook(in);
                // 获取工作簿中的Sheet个数
                int len = tmpWorkBook.getNumberOfSheets();
                if (len <= 1) {
                    XSSFSheet tmpSheet = tmpWorkBook.getSheetAt(0);
                    XSSFSheet newExcelSheet = newExcelWorkBook.createSheet(tmpSheet.getSheetName());
                    // 复制sheet内容
                    copyExcelSheet(newExcelWorkBook, tmpSheet, newExcelSheet);
                } else {
                    for (int i = 0; i < len; i++) {
                        XSSFSheet tmpSheet = tmpWorkBook.getSheetAt(i);
                        XSSFSheet newExcelSheet = newExcelWorkBook.createSheet(tmpSheet.getSheetName());
                        // 复制sheet内容
                        copyExcelSheet(newExcelWorkBook, tmpSheet, newExcelSheet);
                    }
                }
                // 关闭tmpWorkBook工作簿
                tmpWorkBook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 新生成的excel文件
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            fileName += ".xlsx";
        }
        String excelFileName = path + File.separator + fileName;
        // 判断文件是否存在
        File excelFile = new File(excelFileName);
        if (excelFile.exists()) {
            // 存在则删除
            excelFile.delete();
        }
        // 使用输出流写出
        try (FileOutputStream fos = new FileOutputStream(excelFileName)) {
            newExcelWorkBook.write(fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                newExcelWorkBook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("excel文件合并成功，合并后文件路径：" + excelFileName);
    }

    /**
     * #复制sheet到新的excel文件中
     *
     * @param workbook      excel工作簿
     * @param tmpSheet      来源sheet
     * @param newExcelSheet 新生成的sheet
     */
    public static void copyExcelSheet(XSSFWorkbook workbook, XSSFSheet tmpSheet, XSSFSheet newExcelSheet) {
        // 合并单元格
        mergeSheetAllRegion(tmpSheet, newExcelSheet);
        // 设置单元格列宽度
        // 获取最后一个单元格位置
        int len = tmpSheet.getRow(tmpSheet.getFirstRowNum()).getLastCellNum();
        for (int i = 0; i < len; i++) {
            newExcelSheet.setColumnWidth(i, tmpSheet.getColumnWidth(i));
        }
        // 复制每行内容
        Iterator<Row> it = tmpSheet.iterator();
        while (it.hasNext()) {
            XSSFRow tmpRow = (XSSFRow) it.next();
            // 创建新行
            XSSFRow newExcelRow = newExcelSheet.createRow(tmpRow.getRowNum());
            // 复制行
            copyExcelRow(workbook, tmpRow, newExcelRow);
        }
    }

    /**
     * #合并单元格
     *
     * @param tmpSheet      来源sheet
     * @param newExcelSheet 目标sheet
     */
    private static void mergeSheetAllRegion(XSSFSheet tmpSheet, XSSFSheet newExcelSheet) {
        int num = tmpSheet.getNumMergedRegions();
        CellRangeAddress cellRange = null;
        for (int i = 0; i < num; i++) {
            cellRange = tmpSheet.getMergedRegion(i);
            newExcelSheet.addMergedRegion(cellRange);
        }
    }

    /**
     * #复制excel中的行到新的sheet中
     *
     * @param workbook    目标工作簿
     * @param tmpRow      来源excel行
     * @param newExcelRow 目标excel行
     */
    public static void copyExcelRow(XSSFWorkbook workbook, XSSFRow tmpRow, XSSFRow newExcelRow) {
        // 设置行高
        newExcelRow.setHeight(tmpRow.getHeight());
        // 获取所有列
        Iterator<Cell> it = tmpRow.cellIterator();
        while (it.hasNext()) {
            XSSFCell tmpCell = (XSSFCell) it.next();
            // 创建单元格
            XSSFCell newExcelCell = newExcelRow.createCell(tmpCell.getColumnIndex());
            // 复制单元格
            copyExcelCell(workbook, tmpCell, newExcelCell);
        }
    }

    private static XSSFCellStyle xssfCellStyle;

    /**
     * #复制单元格
     *
     * @param workbook     目标工作簿
     * @param tmpCell      来源excel单元格
     * @param newExcelCell 目标excel单元格
     */
    public static void copyExcelCell(XSSFWorkbook workbook, XSSFCell tmpCell, XSSFCell newExcelCell) {
        // 定义 xssfCellStyle常量用来解决 The maximum number of Cell Styles was exceeded.
        // You can define up to 64000 style in a .xlsx Workbook 错误
        if(xssfCellStyle == null) {
            xssfCellStyle = workbook.createCellStyle();
        }
        if(xssfCellStyle instanceof Duplicatable) {
            xssfCellStyle = xssfCellStyle.copy();
        }
        XSSFCellStyle newExcelStyle = xssfCellStyle;
        // 复制单元格样式
        newExcelStyle.cloneStyleFrom(tmpCell.getCellStyle());
        // 单元格样式
        newExcelCell.setCellStyle(newExcelStyle);
        if (tmpCell.getCellComment() != null) {
            newExcelCell.setCellComment(tmpCell.getCellComment());
        }
        // 不同数据类型处理
        CellType tmpCellType = tmpCell.getCellType();

        newExcelCell.setCellType(tmpCellType);
        if (tmpCellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(tmpCell)) {
                newExcelCell.setCellValue(tmpCell.getDateCellValue());
            } else {
                newExcelCell.setCellValue(tmpCell.getNumericCellValue());
            }
        } else if (tmpCellType == CellType.STRING) {
            newExcelCell.setCellValue(tmpCell.getRichStringCellValue());
        } else if (tmpCellType == CellType.BLANK) {
        } else if (tmpCellType == CellType.BOOLEAN) {
            newExcelCell.setCellValue(tmpCell.getBooleanCellValue());
        } else if (tmpCellType == CellType.ERROR) {
            newExcelCell.setCellErrorValue(tmpCell.getErrorCellValue());
        } else if (tmpCellType == CellType.FORMULA) {
            newExcelCell.setCellFormula(tmpCell.getCellFormula());
        } else {
        }
    }


    public static void main(String[] args) {
        //这里是xls文件
        String[] filePaths = {"D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\A_96f0d0ec01084815bfeab48d411a0cdb.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\B_adfccc51867f4aa89586ac5d2593f9c5.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\C_ea854ec1d5194c85872d02bc2d92e370.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\D_65762e71e2914cc28a1189b8f5d0d8fa.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\E_cdbc589b0c074daeaa52252b51fb1239.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\F_005f90b125814c50b7317163a9c1b89a.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\G_6c65b9edca3248ec994a07313c228836.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\H_99c7a4b11ac148b3a1bd07716e2c8934.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\I_87ee201bad5e4ae78aa7c0df1062311d.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\J_87b2cdd5717e446da50fd4be8c91f407.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\K_1ce6fe9e2bd24c96b8a5a31fffbda490.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\L_273b9401765d42c5b5f1b1d2864b5b87.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\M_01735155ecc641dd8d1873b6da44f35c.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\N_665380e934584bf5b7501bf040d59593.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\O_9e00ecc3f28d42f5b79e3441643c6b86.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\P_632eb7370a99449e8147acd30c0c21f0.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\Q_4dd3e60403de4d3a8de9d948f887a555.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\R_48d0411f235f49089e1feb1f02acf62c.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\S_92e733753f2646819591e0775d5d653d.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\T_921a27628ba649b59783654fb87b301d.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\V_472a7493460f46caa40977738ddf68c8.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\W_903a77b2e87740489edf155c53d15a40.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\X_410d3ad909b7432ab2d700abb5076efb.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\Y_59684e038059482d80f8700d938f24e2.xls",
                "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）\\Z_a937d2cd91244209b27bd325c076b229.xls"};
        List<String> list = new ArrayList<>();
        for (String path : filePaths) {
            list.add(path);
        }
        String path = "D:\\DemoExcel\\2022\\11\\全系列车型指导价（汽车之家）";
        String fileName = "全系列车型指导价（汽车之家）.xlsx";
        ExcelUtil.mergeExcel(list, path, fileName);
    }

}
