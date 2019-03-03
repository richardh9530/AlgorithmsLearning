package com.qingwu.test;

import edu.princeton.cs.algs4.Heap;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;

class QingwuTest1 {

    private int index = 1;

    public void method() {
        index++;
        method();
    }


    public void testStackOverflowError() {
        try {
            method();
        } catch (StackOverflowError e) {
            System.out.println("程序所需要的栈大小 > 允许最大的栈大小，执行深度: " + index);
            e.printStackTrace();
        }
    }

    private static final int _1MB=1024*1024;
    /**
     *VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     */
    public static void testAllocation() {
        byte[] allocationl, allocation2, allocation3, allocation4;
        allocationl = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[6 * _1MB];
        allocation4 = new byte[4 * _1MB]; //出现一次Minor GC

    }


    static int i =0;
    public static void main(String[] args) throws  Exception{
        testAllocation();
    }
}


interface inter{
    public static int a = 1;
    void b();
}
abstract class Test implements inter{
    public static int a = 3;
}
public class QingwuTest extends Test{
    public void b(){

    }
    static {
        i = 0;  //  给变量复制可以正常编译通过
        //System.out.print(i);  // 这句编译器会提示“非法向前引用”
    }
    static int i = 1;
    static class DeadLoopClass {

        static {
            System.out.println(Thread.currentThread() + "init DeadLoopClass");
            // 如果不加上这个if语句，编译器将提示“Initializer does not complete normally”并拒绝编译
//            if (true) {
//                System.out.println(Thread.currentThread() + "init DeadLoopClass");
//                while (true) {
//                }
//            }
        }
    }

    public static void main(String[] args){
        Runnable script = new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread() + "start");
                DeadLoopClass dlc = new DeadLoopClass();
                System.out.println(Thread.currentThread() + " run over");
            }
        };

        Thread thread1 = new Thread(script);
        Thread thread2 = new Thread(script);
        thread1.start();
        thread2.start();
    }
}
