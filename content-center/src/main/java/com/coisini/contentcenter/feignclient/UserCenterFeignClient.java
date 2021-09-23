package com.coisini.contentcenter.feignclient;

import com.coisini.contentcenter.configuration.UserCenterFeignConfiguration;
import com.coisini.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @FeignClient(name = "user-center")
 * name 要请求的微服务的名称
 */
// UserCenterFeignConfiguration 已修改为全局配置文件 GlobalFeignConfiguration
//@FeignClient(name = "user-center", configuration = UserCenterFeignConfiguration.class)
@FeignClient(name = "user-center")
public interface UserCenterFeignClient {

    /**
     * Feign通过声明的接口自动构造请求的目标地址完成请求
     * http://user-center/users/{id}
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable Integer id);

}
