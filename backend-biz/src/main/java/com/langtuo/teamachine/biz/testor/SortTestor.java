package com.langtuo.teamachine.biz.testor;

import java.util.Arrays;

public class SortTestor {
    public static void main(String args[]) {
        String result = formatGoodsCode("DFji-Abed1-2Jkl-0b2dfc-ajioKl", "-");
        System.out.println(result);
    }

    public static String formatGoodsCode(String input, String separator) {
        // 1. 使用separator分割字符串
        String[] parts = input.split(String.valueOf(separator));

        // 2. 对分割后的字符串数组进行字母升序排序
        Arrays.sort(parts, String.CASE_INSENSITIVE_ORDER);

        // 3. 使用newSeparator重新拼接排序后的字符串数组
        StringBuffer result = new StringBuffer();
        for (String str : parts) {
            if (result.length() > 0) {
                result.append(separator);
            }
            result.append(str);
        }
        return result.toString();
    }
}
