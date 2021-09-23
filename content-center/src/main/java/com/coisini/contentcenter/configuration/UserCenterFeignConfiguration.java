package com.coisini.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @Description 用户中心 Feign 配置类（已弃用）
 *      这个类不能加@Configuration注解，否则必须挪到@ComponentScan能扫描的包以外
 * @author coisini
 * @date Sep 22, 2021
 * @Version 1.0
 */
public class UserCenterFeignConfiguration {

    @Bean
    public Logger.Level level() {
        // 让feign打印所有请求的细节
        return Logger.Level.FULL;
    }

}
