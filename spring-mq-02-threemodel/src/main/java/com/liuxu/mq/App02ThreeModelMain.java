package com.liuxu.mq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @date: 2025-11-06
 * @author: liuxu
 */
@MapperScan("com.liuxu.mq.dal.mysql")
@SpringBootApplication
public class App02ThreeModelMain {
    public static void main(String[] args) {
        SpringApplication.run(App02ThreeModelMain.class, args);
    }
}
