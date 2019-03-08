package org.study.algorithms.basic.asort;

import org.study.references.basic.algs4.Shell;

import java.util.Arrays;

public class ShellSort extends BaseSort{
    private int hInterval;
    public ShellSort(){
        this.hInterval = 1;
    }
    public ShellSort(int hInterval){
        this.hInterval = hInterval;
    }
    @Override
    public int[] sort(int[] sourceArray){
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int h = 1;
        // arr.length=6时，h=13
        while (h < arr.length) {
            h = h * this.hInterval + 1;  // 确保最后h能够为1？
        }

        while (h > 0) {
            for (int i = h; i < arr.length; i++) { // 这是针对分组，直到h=1
                int tmp = arr[i];
                int j = i - h;
                while (j >= 0 && tmp < arr[j]) {  // 从已经排序的序列最右端开始，这是针对子序列的排序
                    arr[j + h] = arr[j];  // 子序列交换位置
                    j -= h;
                }
                arr[j + h] = tmp;
            }
            System.out.println("h: "+h);
            // 当hInterval=1时需要减一，防止死循环
            h = h/this.hInterval-(this.hInterval==1?1:0 );
        }

        return arr;
    }
}
