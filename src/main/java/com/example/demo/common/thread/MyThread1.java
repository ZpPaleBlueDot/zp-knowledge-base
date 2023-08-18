package com.example.demo.common.thread;

public class MyThread1 implements Runnable{

    private int num;

    public MyThread1() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MyThread1(int num) {
        super();
        this.num = num;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("正在执行task " + num);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task " + num + "执行完毕");
    }

}