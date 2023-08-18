package com.example.demo.entity.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarExcelBO implements Serializable {
    private String url;
    private String carBrand; //汽车品牌
}
