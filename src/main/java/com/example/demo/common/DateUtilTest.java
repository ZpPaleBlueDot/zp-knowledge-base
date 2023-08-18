package com.example.demo.common;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtilTest {
    public static void main(String[] args) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date now = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(now);
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//        String startDay = df.format(calendar.getTime()) + " 00:00:00";
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        String endDay = df.format(calendar.getTime()) + " 23:59:59";
//        System.out.println(startDay + "-" + endDay);

        Map<String, String> map = new HashMap<>();
        map.put("11", "11111");
        map.put("22", "22222");
        List list = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            list.add(map);
        }
        map.put("11", "333333");
        System.out.println(list);
    }
}
