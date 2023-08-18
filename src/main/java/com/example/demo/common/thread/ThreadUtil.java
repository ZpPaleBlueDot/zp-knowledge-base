package com.example.demo.common.thread;

import java.util.concurrent.*;

public class ThreadUtil {

    public void doSomething() {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(5);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5,
                100, TimeUnit.SECONDS, blockingQueue);
        for(int i = 0; i < 10; i++) {
            executor.execute(() -> {

            });
        }
    }

    private static void extracted(BlockingQueue<Runnable> workQueue) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 2,
                TimeUnit.MINUTES, workQueue);
        for (int i = 0; i < 10; i++) {
            try {
                executor.execute(() -> {
                    System.out.println(System.currentTimeMillis() + " " +
                            Thread.currentThread() + "...开始处理,此时workQueue.size:" + workQueue.size());
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println(System.currentTimeMillis() + " " + Thread.currentThread() + "...处理完成");

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        extracted(new LinkedBlockingDeque<>());
        //extracted(new ArrayBlockingQueue<>(5));
        //extracted(new SynchronousQueue<>());

    }


}
