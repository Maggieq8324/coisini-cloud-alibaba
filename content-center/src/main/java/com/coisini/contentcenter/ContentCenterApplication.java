package com.coisini.contentcenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.coisini.contentcenter.configuration.GlobalFeignConfiguration;
import com.coisini.contentcenter.configuration.UserCenterFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
//@EnableFeignClients(defaultConfiguration = GlobalFeignConfiguration.class)
@EnableFeignClients
//@EnableBinding(Source.class)
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
     * @SentinelRestTemplate 为RestTemplate 整合Sentinel
     */
    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
