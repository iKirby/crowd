package com.everyone.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@MapperScan("com.everyone.crowd.dao")
public class CrowdApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdApplication.class, args);
    }
}
