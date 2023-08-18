package com.example.demo.common.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPool {

    /*
     * BlockingQueue workQueue：该线程池中的任务队列：维护着等待执行的Runnable对象
        当所有的核心线程都在干活时，新添加的任务会被添加到这个队列中等待处理，如果队列满了，则新建非核心线程执行任务。
        常用的workQueue类型：

        SynchronousQueue：这个队列接收到任务的时候，会直接提交给线程处理，而不保留它，如果所有线程都在工作怎么办？
        * 那就新建一个线程来处理这个任务！所以为了保证不出现<线程数达到了maximumPoolSize而不能新建线程>的错误，
        * 使用这个类型队列的时候，maximumPoolSize一般指定成Integer.MAX_VALUE，即无限大

        LinkedBlockingQueue：这个队列接收到任务的时候，如果当前线程数小于核心线程数，则新建线程(核心线程)处理任务；
        * 如果当前线程数等于核心线程数，则进入队列等待。由于这个队列没有最大值限制，即所有超过核心线程数的任务都将被添加到队列中，
        * 这也就导致了maximumPoolSize的设定失效，因为总线程数永远不会超过corePoolSize

        ArrayBlockingQueue：可以限定队列的长度，接收到任务的时候，如果没有达到corePoolSize的值，
        * 则新建线程(核心线程)执行任务，如果达到了，则入队等候，如果队列已满，则新建线程(非核心线程)执行任务，
        * 又如果总线程数到了maximumPoolSize，并且队列也满了，则发生错误

        DelayQueue：队列内元素必须实现Delayed接口，这就意味着你传进去的任务必须先实现Delayed接口。
        * 这个队列接收到任务时，首先先入队，只有达到了指定的延时时间，才会执行任务

        链接：https://www.jianshu.com/p/ae67972d1156
     */
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, Integer.MAX_VALUE,
                60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        //往线程池中循环提交线程
        for (int i = 0; i < 10; i++) {
            //创建线程类对象
            MyThread1 myTask = new MyThread1(i);
            //开启线程
            executor.execute(myTask);
            //获取线程池中线程的相应参数
            System.out.println("线程池中线程数目：" + executor.getPoolSize() +
                    "，队列中等待执行的任务数目：" + executor.getQueue().size() +
                    "，已执行完的任务数目：" + executor.getCompletedTaskCount());
        }
        try {
            System.out.println("==1==线程池中线程数目：" + executor.getPoolSize() +
                    "，队列中等待执行的任务数目：" + executor.getQueue().size() +
                    "，已执行完的任务数目：" + executor.getCompletedTaskCount());
            Thread.sleep(6000);
            System.out.println("==2==线程池中线程数目：" + executor.getPoolSize() +
                    "，队列中等待执行的任务数目：" + executor.getQueue().size() +
                    "，已执行完的任务数目：" + executor.getCompletedTaskCount());
            System.out.println("执行另一批线程----");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //往线程池中循环提交线程
        for (int i = 0; i < 6; i++) {
            //创建线程类对象
            MyThread1 myTask = new MyThread1(i);
            //开启线程
            executor.execute(myTask);
            //获取线程池中线程的相应参数
            System.out.println("2线程池中线程数目：" + executor.getPoolSize() +
                    "，队列中等待执行的任务数目：" + executor.getQueue().size() +
                    "，已执行完的任务数目：" + executor.getCompletedTaskCount());
        }
        //待线程池以及缓存队列中所有的线程任务完成后关闭线程池。
        executor.shutdown();
        System.out.println("关闭线程池----");
        try {
            Thread.sleep(20000);
            System.out.println("主线程end----");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("==3==线程池中线程数目：" + executor.getPoolSize() +
                "，队列中等待执行的任务数目：" + executor.getQueue().size() +
                "，已执行完的任务数目：" + executor.getCompletedTaskCount());
    }
}

