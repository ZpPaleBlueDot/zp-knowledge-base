package com.example.demo.common;

import java.util.UUID;

public class createUUID {
    public static void main(String[] args) {
        for(int i = 0; i < 3; i++) {
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            System.out.println(id);
        }
    }
}
