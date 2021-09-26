package com.coisini.contentcenter.feignclient.fallbackfactory;

import com.coisini.contentcenter.feignclient.TestFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * TestFeignClient Sentinel 的远程调用异常捕捉
 */
@Component
@Slf4j
public class TestFeignClientFallbackFactory implements FallbackFactory<TestFeignClient> {
    @Override
    public TestFeignClient create(Throwable cause) {
        return new TestFeignClient() {
            @Override
            public String test(String name) {
                log.warn("远程调用被限流/降级了", cause);
                return "远程调用被限流/降级了";
            }
        };
    }
}
