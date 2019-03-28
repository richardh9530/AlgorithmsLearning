package me.richardh9530.test;

import java.io.IOException;

public class ThreadTest {
    public static void main(String[] args){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<1000;i++){
                    System.out.println("This is 1-"+i);
                }

            }
        }).run();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<1000;i++){
                    System.out.println("This is 2-"+i);
                }
            }
        }).start();
        System.out.println("线程启动了");
        int $2 = 3;
        int _2 = 3;
        try{
            int  i =2;
            throw new IOException();
        }
        catch (IOException e){
            System.out.println("");
        }
        finally{
            //
            System.out.println("fff");
        }

    }
}
