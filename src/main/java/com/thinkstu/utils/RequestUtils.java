package com.thinkstu.utils;

import com.thinkstu.entity.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/3, 17:44, 周一
 **/
@Component
public class RequestUtils {
    @Autowired
    OkHttpClient client;
    @Autowired
    CookieUtils cookieUtils;

    @Value("${user_agent}")
    String user_agent;
    @Value("${type}")
    String type;
    @Value("${url}")
    String url;
    @Value("${referer}")
    String referer;

    /**
     * 发送请求
     *
     * @param cookie cookie值
     * @param param  参数实体对象
     * @return
     * @throws Exception
     */
    public String post(String cookie, ParamEntity param) {
        String paramString = new StringBuilder()
                .append("XXXQDM=").append(param.getXXXQDM())
                .append("&JASLXDM=02&KXRQ=").append(param.getDate())
                .append("&KSJC=").append(param.getKSJC())
                .append("&JSJC=").append(param.getJSJC())
                .toString();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse(type), paramString))
                .addHeader("User-Agent", user_agent)
                .addHeader("Content-Type", type)
                .addHeader("Referer", referer)
                .addHeader("Cookie", cookie)
                .build();
        // TODO 下面这里，以后需要处理异常
        String data = "";
        try {
            Response response = client.newCall(request).execute();
            data = response.body().string();
            response.close();
        } catch (Exception e) {
        }
        return data;
    }
}
