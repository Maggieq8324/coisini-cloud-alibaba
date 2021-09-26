package com.coisini.contentcenter.feignclient.fallback;

import com.coisini.contentcenter.feignclient.TestFeignClient;
import org.springframework.stereotype.Component;

/**
 * TestFeignClient 被流控时的处理逻辑
 */
@Component
public class TestFeignClientFallback implements TestFeignClient {
    @Override
    public String test(String name) {
        // 业务逻辑
        return "流控/降级";
    }
}
