package com.coisini.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @Description Feign全局配置类
 *      这个类不能加@Configuration注解，否则必须挪到@ComponentScan能扫描的包以外
 * @author coisini
 * @date Sep 22, 2021
 * @Version 1.0
 */
public class GlobalFeignConfiguration {

    @Bean
    public Logger.Level level() {
        // 让feign打印所有请求的细节
        return Logger.Level.FULL;
    }

}
