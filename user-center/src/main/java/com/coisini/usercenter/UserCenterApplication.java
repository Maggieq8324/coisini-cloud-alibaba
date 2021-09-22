package com.coisini.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description 启动类
 * @author coisini
 * @date Sep 20, 2021
 * @Version 1.0
 */
@MapperScan("com.coisini.usercenter.dao")
@SpringBootApplication
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
