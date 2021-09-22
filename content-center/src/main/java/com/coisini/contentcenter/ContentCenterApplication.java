package com.coisini.contentcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description
 * @author coisini
 * @date
 * @Version 1.0
 */
@MapperScan("com.coisini.contentcenter.dao")
@SpringBootApplication
public class ContentCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    /**
     * 在Spring容器中，创建一个对象，类型RestTemplate， 名称/ID是restTemplate
     * <bean id="restTemplate" class ="xxx.RestTemplate" />
     *
     * @LoadBalanced 为RestTemplate 整合Ribbon
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
