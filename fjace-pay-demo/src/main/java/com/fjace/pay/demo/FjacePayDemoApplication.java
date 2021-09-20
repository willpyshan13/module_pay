package com.fjace.pay.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(basePackages = {"com.fjace"})
@MapperScan("com.fjace.pay.service.mapper")
@Configuration
public class FjacePayDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FjacePayDemoApplication.class, args);
    }

}
