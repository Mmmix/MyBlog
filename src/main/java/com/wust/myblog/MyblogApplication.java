package com.wust.myblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com.wust")
@MapperScan("com.wust.myblog.mapper")
@SpringBootApplication
public class MyblogApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder app){
        return app.sources(MyblogApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
    }
}
