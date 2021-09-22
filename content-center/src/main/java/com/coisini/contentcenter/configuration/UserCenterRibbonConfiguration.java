package com.coisini.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

/**
 * @Description 用户中心配置类
 * @author coisini
 * @date Sep 22, 2021
 * @Version 1.0
 */
@Configuration
//@RibbonClient(name = "user-center", configuration = RibbonConfiguration.class)
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {
}
