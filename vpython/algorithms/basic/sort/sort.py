import sys
import copy


def bubble_sort(arr):
    _sorted_arr = copy.copy(arr)
    for i in range(1, len(_sorted_arr)):  # 两两比较，大的浮在后边，共比较n-1次
        for j in range(0, len(_sorted_arr)-i):
            if _sorted_arr[j] > _sorted_arr[j+1]:  # 如果第j个元素大于后一个，则两者交换
                _sorted_arr[j], _sorted_arr[j+1] = _sorted_arr[j+1], _sorted_arr[j]
    return _sorted_arr


def selection_sort(arr):
    _sorted_arr = copy.copy(arr)
    for i in range(len(_sorted_arr)-1):  # 一共找n-1次，因为最后一次只剩最后一个元素了
        min_index = i
        for j in range(i+1, len(_sorted_arr)):  # 从已排好序的元素以后开始找
            if _sorted_arr[j] < _sorted_arr[min_index]:
                min_index = j
        if min_index != i:  # 下标不一致则交换值
            _sorted_arr[min_index], _sorted_arr[i] = _sorted_arr[i], _sorted_arr[min_index]
    return _sorted_arr


def insertion_sort(arr):
    _sorted_arr = copy.copy(arr)
    for i in range(1, len(_sorted_arr)):  # 把前i个看成是有序序列
        to_be_inserted_value = _sorted_arr[i]  # 要插入的值
        j = i
        while j > 0 and to_be_inserted_value < _sorted_arr[j-1]:
            # 将要插入的值与有序序列的各个值逐一比较， 若小于，则有序序列整体向右移动
            _sorted_arr[j] = _sorted_arr[j-1]
            j -= 1
        if j != i:
            _sorted_arr[j] = to_be_inserted_value
    return _sorted_arr


def shell_sort(arr, h_interval=3):
    _sorted_arr = copy.copy(arr)
    h = 1
    while h < len(_sorted_arr):
        h = h*h_interval + 1
    while h > 0:
        for i in range(h, len(_sorted_arr)):  # 左边的是有序的
            j = i-h
            to_be_inserted_value = _sorted_arr[j]


if __name__ == '__main__':
    to_be_sorted_array = [10,12,3,5,8,7,9]
    after_sorted_array = insertion_sort(to_be_sorted_array)
    print(after_sorted_array)