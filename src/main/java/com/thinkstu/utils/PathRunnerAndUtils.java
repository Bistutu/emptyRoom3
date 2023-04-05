package com.thinkstu.utils;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.boot.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.regex.*;

/**
 * Runner：程序初始化时自动创建数据保存目录
 * Utils：工具类，获取路径
 */

@Slf4j
@Data
@Component
public class PathRunnerAndUtils implements CommandLineRunner {
    private String path;
    private String user_dir;
    private String cookie_file;

    @Override
    public void run(String... args) {
        user_dir = System.getProperty("user.dir");
        cookie_file = user_dir + "/cookie.txt";
        if (checkVirtual()) {
            path = user_dir + "/static/empty/";
        } else {
            path = "/Users/thinkstu/Desktop/empty/";
        }
        // 不存在则新建目录
        new File(path).mkdirs();
        new File(path + "1").mkdirs();
        new File(path + "2").mkdirs();
        new File(path + "3").mkdirs();
        new File(path + "4").mkdirs();
    }

    /**
     * 检测本地环境是否为虚拟机，作用：兼容开发环境与线上环境的路径
     */
    public boolean checkVirtual() {
        Pattern pattern = Pattern.compile(".*(Virtual|Cloud|KVM).*");
        try {
            Process        process = Runtime.getRuntime().exec("grep DMI /var/log/dmesg");
            BufferedReader reader  = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String         line    = reader.readLine();
            if (pattern.matcher(line).matches()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
