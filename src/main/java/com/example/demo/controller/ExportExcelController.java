package com.example.demo.controller;

import com.example.demo.entity.bo.CarExcelBO;
import com.example.demo.service.ExportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/excel")
public class ExportExcelController {
    @Autowired
    ExportExcelService exportExcelService;

    @PostMapping("/car")
    public String orgRegisterForWeb(@RequestBody CarExcelBO carExcelBO) {
        return exportExcelService.exportExcel(carExcelBO);
    }

}
