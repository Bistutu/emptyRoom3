package com.thinkstu.config;

import com.thinkstu.utils.*;
import okhttp3.*;
import org.springframework.context.annotation.*;

import java.util.concurrent.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/3, 17:35, 周一
 **/
@Configuration
public class OkhttpConfig {
    @Bean
    OkHttpClient okHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
//                .connectionPool(new ConnectionPool(8, 5, TimeUnit.MINUTES))
                .addInterceptor(new RetryInterceptor(3, 1000))
                .build();
    }
}
