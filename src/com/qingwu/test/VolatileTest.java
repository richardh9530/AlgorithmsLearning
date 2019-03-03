package com.qingwu.test;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileTest {

    // public static volatile int race = 0;
    public static AtomicInteger race = new AtomicInteger(0);

    public synchronized static void increase() {
        //race++;
        race.incrementAndGet();
    }

    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) {
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < 10000; j++) {
                            increase();
                        }
                    }
                }
            );
            threads[i].start();
        }

        System.out.println("initial phase--active count: " + Thread.activeCount());
        // 等待所有累加线程都结束
        while(Thread.activeCount()>2) {
            Thread.yield();
            System.out.println("total active threads: " + Thread.activeCount());
        }
        System.out.println("final race: "+race);

    }
}
