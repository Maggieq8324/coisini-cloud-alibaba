package com.coisini.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-center")
public interface TestFeignClient {

    /**
     * test接口被调用时，feign会构造出 url
     * http://user-center/test/{name} 完成请求
     * @param name
     * @return
     */
    @GetMapping("/test/{name}")
    String test(@PathVariable String name);

}
