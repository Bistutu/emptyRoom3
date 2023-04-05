package com.thinkstu;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

@SpringBootTest
class Tests {
    @Value("${user_agent}")
    String user_agent;
    @Value("${type}")
    String type;
    @Value("${url}")
    String url;
    @Value("${referer}")
    String referer;

    @Test
    void contextLoads() {
        System.out.println(type);
    }

}
