package com.azer.disruptordemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.azer.disruptordemo.mapper")
public class DisruptorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisruptorDemoApplication.class, args);
    }

}
