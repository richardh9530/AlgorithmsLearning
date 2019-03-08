package org.study.algorithms.basic.asort;

import java.util.Arrays;

public class HeapSort extends BaseSort{

    @Override
    public int[] sort(int[] sourceArray) {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int len = arr.length;

        buildMaxHeap(arr, len);
        System.out.println("build heap done!");

        for (int i = len - 1; i > 0; i--) {
            swap(arr, 0, i);  // 将堆顶元素（最大值）和剩余堆中最后一个值互换
            len--;
            heapify(arr, 0, len);
        }
        return arr;
    }

    private void buildMaxHeap(int[] arr, int len) {
        for (int i = len / 2; i >= 0; i--) {  // 对每一个非叶子结点，调整堆
            heapify(arr, i, len);
        }
    }

    /**
     * 构建堆
     * 1.首先将所有元素按照初始顺序填充到一个完全二叉树中
     * 2.从“最后一个非终端节点”(最后一个非叶子节点)开始，调用siftdown方法，调整堆的结构，直到根节点为止
     * 当前元素若可能与下一层元素交换，就是siftdown；若可能与上一层元素交换，就是siftup。
     */
    private void heapify(int[] arr, int i, int len) {
        /*
         * 以6个元素为例，初始时i=3; len=6
         * 此时left = 7 right=8
         */
        int left = 2 * i + 1;  // 左儿子
        int right = 2 * i + 2;  // 右儿子
        int largest = i;  // 假定最大的是根节点

        if (left < len && arr[left] > arr[largest]) {  // 比较左儿子与根节点
            largest = left;
        }

        if (right < len && arr[right] > arr[largest]) {  // 比较右儿子与根节点
            largest = right;
        }

        if (largest != i) {  // 这个时候根节点小于于左右儿子，largest 可能左，也可能右
            swap(arr, i, largest);
            System.out.println("adjust heap: "+ arr[i] + " --> "+ arr[largest]);
            heapify(arr, largest, len);  // 调整与原来根节点对换位置的那个儿子及其子孙
            // （初始时这里是叶子节点是没有儿子的，但是到了后来递归调整到堆顶的时候就有子孙了）
            // 只要有节点位置动了，那么就要和他的儿子们进行比较下去，直到没有调整为止
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
