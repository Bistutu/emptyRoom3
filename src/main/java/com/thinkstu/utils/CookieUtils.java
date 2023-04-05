package com.thinkstu.utils;

import com.alibaba.fastjson2.*;
import com.thinkstu.entity.*;
import lombok.extern.slf4j.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/4, 08:49, 周二
 * 从文件中读取 cookie
 **/
@Slf4j
@Component
public class CookieUtils {
    @Autowired
    OkHttpClient client;
    @Autowired
    PathRunnerAndUtils pathUtils;

    /**
     * 该 Cookie 类的作用应该为：从文件中读取 Cookie 值，但是不应该连续使用（耗资源）
     */
    public String get() {
        String emptyCookie = "";
        try {
            String         path   = System.getProperty("user.dir") + "/cookie.txt";
            FileReader     reader = new FileReader(path);
            BufferedReader in     = new BufferedReader(reader);
            emptyCookie = in.readLine();
        } catch (Exception e) {
            // 什么也不做
        }
        return emptyCookie;
    }

    /**
     * 测试：检查 cookie 的值<br>
     * 如果过期我们会更新该 Cookie 的值<br>
     * 该函数的作用：保持 cookie 最新！
     */
    public void check(String cookie) {
        // 测试：尝试导出一次空教室数据
        Request request = new Request.Builder()
                .url("https://jwxt.bistu.edu.cn/jwapp/sys/kxjas/modules/kxjscx/cxkxjs.do")
                .method("POST", RequestBody.create(MediaType.parse("text/plain"),
                        "querySetting=[]&pageSize=1&pageNumber=1"))
                .addHeader("Cookie", cookie)
                .build();
        // cookie 的 3 种状态：1、不需要更新，2、更新成功，3、更新失败（发生异常）
        try (Response response = client.newCall(request).execute()) {
            // 如果 Cookie 失效，则调用函数更新 cookie
            if (!response.header("X-Frame-Options").equals("SAMEORIGIN")) {
                this.update();
            } else {
                log.info("===》cookie 不需要更新");
                return;
            }
        } catch (Exception e) {
            log.error("===》发生异常，cookie 更新失败！！！");
        }
        log.info("===》cookie 更新成功");
    }

    /**
     * 更新 cookie 的值
     */
    void update() throws IOException {
        Request request = new Request.Builder()
                .url("http://bistu.thinkstu.com/bistu/empty"
                        + "?username=2018010426"
                        + "&password=Bistu123456")
                .build();
        Response response    = client.newCall(request).execute();
        String   emptyCookie = JSON.parseObject(response.body().string(), CookieEntity.class).getEmptyCookie();
        response.close();   // 释放资源
        // 将新 cookie 写入文件
        File       file = new File(pathUtils.getCookie_file());
        FileWriter fw   = new FileWriter(file, false);
        fw.write(emptyCookie);
        fw.close();
    }
}
