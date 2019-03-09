
def multi_large_numbers(str_num_1, str_num_2):
    """
    输入两个大数，输出二者的乘积，用数组表示
    :param str_num_1:
    :param str_num_2:
    :return:
    """
    num_1 = [int(num) for num in reversed(str_num_1)]  # 先逆序，使得两个数低端对齐
    num_2 = [int(num) for num in reversed(str_num_2)]
    num_res = [0 for _ in range(len(num_1)+len(num_2))]
    for i, num_i in enumerate(num_1):
        for j, num_j in enumerate(num_2):
            num_res[i+j] += num_i*num_j
    for k, val in enumerate(num_res):
        if val > 9:
            num_res[k+1] += val//10
            num_res[k] = val % 10
    flag = True
    zero = 0
    num_res = list(reversed(num_res))  # num_res 从低位开始排列
    for num in num_res:
        if flag is True and num == 0:  # 直到第一个非零数
            zero += 1
        else:
            flag = False
    num_res = num_res[zero:]
    final_res = "".join([str(i) for i in num_res])
    assert int(str_num_1)*int(str_num_2) == int(final_res)
    return final_res


def add_large_numbers(str_num_1, str_num_2):
    """
    输入两个大数，输出二者的和
    :param str_num_1:
    :param str_num_2:
    :return:
    """
    num_1 = [int(num) for num in reversed(str_num_1)]  # 先逆序，使得两个数低端对齐
    num_2 = [int(num) for num in reversed(str_num_2)]
    num_res = [0 for _ in range(max(len(num_1), len(num_2))+ 1)]
    min_len = min(len(num_1), len(num_2))
    for i in range(min_len):
        num_res[i] += num_1[i] + num_2[i]

    for j in range(min_len, len(num_1)):
        num_res[j] += num_1[j]
    for j in range(min_len, len(num_2)):
        num_res[j] += num_2[j]

    for k, val in enumerate(num_res):
        if val > 9:
            num_res[k+1] += val//10
            num_res[k] = val % 10
    flag = True
    zero = 0
    num_res = list(reversed(num_res))  # num_res 从低位开始排列
    for num in num_res:
        if flag is True and num == 0:  # 直到第一个非零数
            zero += 1
        else:
            flag = False
    num_res = num_res[zero:]
    final_res = "".join([str(i) for i in num_res])
    assert int(str_num_1)+int(str_num_2) == int(final_res)
    return final_res


def factorial_large_number(num):
    if num == 1:
        return 1
    else:
        return multi_large_numbers(str(num), str(factorial_large_number(num-1)))


print(multi_large_numbers('03023','033'))

print(add_large_numbers('33023','33'))

print(factorial_large_number(7))