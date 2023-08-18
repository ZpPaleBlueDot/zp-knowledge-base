package com.example.demo.common.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    //    0. 将创建对象的操作提取成变量，防止在 map 方法重复创建
    private Text text = new Text();
    private IntWritable i = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1. 将 Hadoop 内置的text 数据类型转换为string类型
        // 方便操作
        String str = value.toString();

        // 2. 对字符串进行切分
        String[] split = str.split(" ");

        // 3. 对字符串数组遍历，将单词映射成 （单词，1）
        for (String s : split) {
            text.set(s);
            context.write(text, i);
        }
    }
}
