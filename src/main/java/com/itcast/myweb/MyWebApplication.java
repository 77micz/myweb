package com.itcast.myweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
//扫描com.itcast.myweb包和com.packages包下的所有类
@ComponentScan(basePackages = {"com.itcast.myweb","com.packages"})
public class MyWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWebApplication.class, args);
    }

}
