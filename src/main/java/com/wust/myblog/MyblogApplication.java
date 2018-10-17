package com.wust.myblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com.wust")
@MapperScan("com.wust.myblog.mapper")
@SpringBootApplication
public class MyblogApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
    }
}
