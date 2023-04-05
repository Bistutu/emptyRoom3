package com.thinkstu.config;

import org.springframework.context.annotation.*;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {
    @Bean
    public ExecutorService threadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                8,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(500),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
