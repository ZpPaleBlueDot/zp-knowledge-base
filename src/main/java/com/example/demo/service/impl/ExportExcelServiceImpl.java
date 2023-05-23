package com.example.demo.service.impl;

import com.example.demo.entity.bo.CarExcelBO;
import com.example.demo.service.ExportExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("exportExcelService")
public class ExportExcelServiceImpl implements ExportExcelService {

    @Override
    public String exportExcel(CarExcelBO carExcelBO) {
        //第1页地址
        String url = carExcelBO.getUrl();
        // 发送http请求
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return "导出异常";
        }
        List<List> list = new ArrayList<>();
        Elements div = document.select("div[class=list-cont]");
        div.forEach(e -> {
            // 获取车系
            String carSeries = e.select("div[class=list-cont-main] div[class=main-title] a[class=font-bold]").text();
            String value = e.attr("data-value");
            Elements lis = document.select("div[id=divSpecList"+value+"] ul[class=interval01-list] li[data-value]");
            lis.forEach(li -> {
                // 获取车型
                String carType = li.select("div[class=interval01-list-cars] p a").text();
                // 获取车型指导价
                String guidance = li.select("div[class=interval01-list-guidance] div").text();
                List<String> strList = new ArrayList<>();
                strList.add(carExcelBO.getCarBrand());
                strList.add(carSeries);
                strList.add(carType);
                strList.add(guidance);
                list.add(strList);
            });
        });
        String[] strArr = {"品牌","车系","车型","指导价"};
        return writeToExcelByList(strArr, list);
    }

    public static String writeToExcelByList(String[] array, List<List> list) {
        //创建工作薄
        Workbook wb = new XSSFWorkbook();
        //标题和页码
        CellStyle titleStyle = wb.createCellStyle();
        // 设置单元格对齐方式,水平居左
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        // 设置字体样式
        Font titleFont = wb.createFont();
        // 字体高度
        titleFont.setFontHeightInPoints((short) 12);
        // 字体样式
        titleFont.setFontName("黑体");
        titleStyle.setFont(titleFont);
        //创建sheet
        Sheet sheet = wb.createSheet("汽车数据");
        // 自动设置宽度
        sheet.autoSizeColumn(0);
        // 在sheet中添加标题行// 行数从0开始
        Row row = sheet.createRow((int) 0);
        for (int i = 0; i < array.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(array[i]);
            cell.setCellStyle(titleStyle);
        }
        // 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
        CellStyle dataStyle = wb.createCellStyle();
        // 设置居中样式，水平居中
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        //数据从序号1开始
        try {
            int index = 1;
            for (List value : list) {
                // 默认的行数从0开始，为了统一格式设置从1开始，就是从excel的第二行开始
                row = sheet.createRow(index);
                index++;
                List data = value;
                for (int j = 0; j < data.size(); j++) {
                    Cell cell = row.createCell(j);
                    // 为当前列赋值
                    cell.setCellValue(data.get(j).toString());
                    //设置数据的样式
                    cell.setCellStyle(dataStyle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileOutputStream fileOutputStream;
        try {
            String filepath = "D:/DemoExcel/" + new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
            File f = new File(filepath);// 创建文件夹路径
            // 如果文件夹不存在则创建
            if (!f.exists() && !f.isDirectory()){
                f.mkdirs();
            }
            String filename = String.format("%s_%s.xls", "car", UUID.randomUUID().toString().replaceAll("-", ""));
            File file = new File(f, filename);
            if (!file.exists()){
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            wb.write(fileOutputStream);
            fileOutputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "导出成功";
    }

    public static void main(String[] args) throws Exception {
        String[] words = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","V","W","X","Y","Z"};
        List<List> list = new ArrayList<>();
        String url = "https://www.autohome.com.cn/grade/carhtml/Z.html";
        Document document = Jsoup.connect(url).get();
        Elements dls = document.select("dl");
            dls.forEach(dl -> {
                String carBrand = dl.select("dt div a").text();
                Elements divs = dl.select("dd div[class=h3-tit]");
                Elements uls = dl.select("dd ul[class=rank-list-ul]");
                int s = divs.size();
                for(int i = 0; i < s; i++) {
                    String carShop = divs.get(i).select("a").text();
                    Elements lis = uls.get(i).select("li");
                    lis.forEach(li -> {
                        String carSeries = li.select("h4 a").text();
                        String typeUrl = li.select("h4 a").attr("href");
                        if(!StringUtil.isBlank(typeUrl)) {
                            try {
                                Document doc = Jsoup.connect("https:" + typeUrl).get();
                                Elements es = doc.select("div[class=spec-wrap active]");
                                Elements dds = es.select("dl dd");
                                if(dds != null && dds.size() > 0) {
                                    dds.forEach(dd -> {
                                        // 获取车型
                                        String carType = dd.select("div[class=spec-name] div[class=name-param] p a").text();
                                        // 获取车型指导价
                                        String guidance = dd.select("div[class=spec-guidance] div[class=guidance-price__con] span").text();
                                        List<String> strList = new ArrayList<>();
                                        strList.add("Z");
                                        strList.add(carBrand);
                                        strList.add(carShop);
                                        strList.add(carSeries);
                                        strList.add(carType);
                                        strList.add(guidance);
                                        list.add(strList);
                                    });
                                } else {
                                    List<String> strList = new ArrayList<>();
                                    strList.add("Z");
                                    strList.add(carBrand);
                                    strList.add(carShop);
                                    strList.add(carSeries);
                                    strList.add("");
                                    strList.add("");
                                    list.add(strList);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            });
        String[] strArr = {"字母","品牌","厂商","车系","车型","指导价"};
        System.out.println(ExportExcelServiceImpl.writeToExcelByList(strArr, list));
    }

}
