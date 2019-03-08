package org.study.algorithms.basic.asort;

import java.util.Arrays;

public class InsertionSort extends BaseSort{
    @Override
    public int[] sort(int[] sourceArray) {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        // 从下标为1的元素开始选择合适的位置插入，因为下标为0的只有一个元素，默认是有序的
        for (int i = 1; i < arr.length; i++) {

            // 记录要插入的数据
            int tmp = arr[i];

            // 从已经排序的序列最右边的开始比较，找到比其小的数
            int j = i-1;
            while (j >= 0 && tmp < arr[j]) {
                arr[j+1] = arr[j];
                j--;
            }

            // 存在比其小的数，插入
            if (j != i-1) {
                arr[j+1] = tmp;
            }

        }
        return arr;
    }
}
