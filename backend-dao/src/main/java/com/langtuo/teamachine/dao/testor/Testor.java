package com.langtuo.teamachine.dao.testor;

import org.assertj.core.util.Lists;

import java.util.List;

public class Testor {
    public static void main(String args[]) {
        List<String> listA = Lists.newArrayList("1", "2", "3");
        List<String> listB = Lists.newArrayList("a", "b", "c");
        List<String> listC = Lists.newArrayList("A", "B", "C");
        List<List<String>> lists = Lists.newArrayList(listA, listB, listC);

        List<String> resultList = Lists.newArrayList();
        genComb(lists, 0, null, resultList);
        for (String result : resultList) {
            System.out.println(result);
        }
    }

    public static void genComb(List<List<String>> lists, int index, String prefix, List<String> resultList) {
        if (prefix == null) {
            prefix = "";
        }
        if (index == lists.size()) {
            resultList.add(prefix);
            return;
        }

        List<String> list = lists.get(index);
        for (String s : list) {
            genComb(lists, index + 1, prefix + s, resultList);
        }
    }
}
