package org.study.algorithms.basic.asort;

import org.study.references.basic.algs4.Heap;

abstract class BaseSort {
    // int[] toBeSortedArray = {1,3,5,7,4,2,28,37,6,0};
    int[] toBeSortedArray = {5,1,3,7,4,8};

    public abstract int[] sort(int[] sourceArray);

    public void testSort(){
        int[] sortedArray = this.sort(toBeSortedArray);
        for(int i=0;i<sortedArray.length;i++){
            System.out.println(sortedArray[i]);
        }
    }
}

public class TestSort {
    public static void main(String[] args){
         new RadixSort().testSort();
    }
}

