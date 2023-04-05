package com.thinkstu;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.scheduling.annotation.*;

@SpringBootApplication
@EnableScheduling
public class EmptyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmptyApplication.class, args);
    }

}
