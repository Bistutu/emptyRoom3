package com.thinkstu.config;

import java.time.*;
import java.time.format.*;
import java.util.regex.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/3, 18:26, 周一
 **/
public class test {
    public static void main(String[] args) {
//        Pattern pattern = Pattern.compile(".*(Virtual|Cloud|KVM).*");
//        System.out.println( pattern.matcher("[    0.000000] DMI: Microsoft Corporation Virtual Machine/Virtual Machine, BIOS 090007  05/18/2018").matches());

        LocalDateTime    now = LocalDateTime.now(); // 当前日期和时间
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        String            dt  = dtf.format(now);
        System.out.println(dt);
    }
}
