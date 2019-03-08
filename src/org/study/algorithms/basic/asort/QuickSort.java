package org.study.algorithms.basic.asort;

import java.util.Arrays;

public class QuickSort extends BaseSort {
    @Override
    public int[] sort(int[] sourceArray) {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        return quickSort(arr, 0, arr.length - 1);
    }

    private int[] quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int partitionIndex = partition(arr, left, right);
            quickSort(arr, left, partitionIndex - 1);  // 比它小的放左边
            quickSort(arr, partitionIndex + 1, right);  // 比它大的放右边
        }
        return arr;
    }

    private int partition(int[] arr, int left, int right) {
        // 设定基准值（pivot）
        int pivot = left;
        int index = pivot + 1;
        for (int i = index; i <= right; i++) { // 基准值右边的元素，若小于基准值，则放到基准值右边？
            if (arr[i] < arr[pivot]) {
                swap(arr, i, index);
                index++;
            }
        }
        swap(arr, pivot, index - 1);  // 基准值和小于基准值的最大元素换位置
        // 这样上边循环后基准值右边小于基准值的就都到了基准值左边，此时基准值的index变为了index-1
        return index - 1;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
