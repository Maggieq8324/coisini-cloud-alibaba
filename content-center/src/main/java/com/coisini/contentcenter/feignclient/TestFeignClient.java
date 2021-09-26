package com.coisini.contentcenter.feignclient;

import com.coisini.contentcenter.feignclient.fallback.TestFeignClientFallback;
import com.coisini.contentcenter.feignclient.fallbackfactory.TestFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description
 *      fallback = TestFeignClientFallback.class
 *      一旦远程调用被流控了就会进入TestFeignClientFallback
 *
 *      fallbackFactory Sentinel 限流降级的异常捕捉处理类
 *      fallback 与 fallbackFactory 二选一用
 * @author coisini
 * @date
 * @Version 1.0
 */
@FeignClient(name = "user-center",
//        fallback = TestFeignClientFallback.class,
        fallbackFactory = TestFeignClientFallbackFactory.class)
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
