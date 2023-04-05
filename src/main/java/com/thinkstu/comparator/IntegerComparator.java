package com.thinkstu.comparator;


import java.util.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/4, 16:38, 周二
 **/
public class IntegerComparator implements Comparator<java.lang.String> {
    @Override
    public int compare(java.lang.String o1, java.lang.String o2) {
        int num1 = Integer.parseInt(o1);
        int num2 = Integer.parseInt(o2);
        return Integer.compare(num1, num2);
    }
}
