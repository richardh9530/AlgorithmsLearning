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
        int index = pivot + 1;  // index 其实是指示基准值右侧所有已经确定比基准值小的元素列表的下一个位置
        // 也就是说，一开始index指向基准值的下一个值，那么第一次比较如果比基准值小，就原地不动【i==index】
        // 然后index 指针右移【这个含义就是index往前的肯定是比基准值小的，所以不用管了，index
        // 指向的这个位置应该是不小于基准值的，因为假如小于的话index指针就右移了】
        // 这个时候循环还在继续，遇到比基准值小的就跟index换位置，然后index指针右移
        // 到了for循环停止的时候，index仍然指向大于或等于基准值的第一个元素位置
        // index-1及其往左，都是确定比基准值小的，所以
        // 关键一步，最后，index-1和pivot交换位置
        // 这样基准值的下标就成为了index-1
        // 考虑极端的情况，基准值右边的都比基准值小，那么最终index++ 后，index应该等于right+1了
        // 【因为这个时候i其实是和index同步增加的i++, index++】
        // 所以这一极端情况下pivot就指向了最后一个元素，左边都是比基准值小的，右边没有元素，或者说是大于等于基准值的
        for (int i = index; i <= right; i++) { // 基准值右边的元素，若小于基准值，则放到基准值右边？
            if (arr[i] < arr[pivot]) {
                swap(arr, i, index);
                index++;
            }
        }
        swap(arr, pivot, index - 1);  // 基准值和小于基准值的最大元素换位置
        // 这样上边循环后基准值右边小于基准值的就都到了基准值左边，此时基准值的下标变为了index-1
        return index - 1;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
