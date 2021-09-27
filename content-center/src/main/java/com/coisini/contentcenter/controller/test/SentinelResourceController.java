package com.coisini.contentcenter.controller.test;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.coisini.contentcenter.sentinel.TestBlockHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description @SentinelResource 使用示例
 * @author coisini
 * @date
 * @Version 1.0
 */
@RestController
@RequestMapping("/sentinel_resource")
@Slf4j
public class SentinelResourceController {

    /**
     * @SentinelResource 注解不支持来源
     */
    @GetMapping("/test")
    @SentinelResource(
            value = "sentinel_resource_test",
            blockHandler = "block",
            blockHandlerClass = TestBlockHandler.class,
            fallback = "fallback"
    )
    public String test(@RequestParam(required = false) String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name不能为空");
        }
        return name;
    }

//    /**
//     * 处理限流或降级
//     * @param a
//     * @param e
//     * @return
//     */
//    public String block(String a, BlockException e) {
//        log.warn("限流，或者降级了 block", e);
//        return "限流，或者降级了 block";
//    }

    /**
     * 处理降级
     * @param a
     * @return
     */
    public String fallback(String a) {
        log.warn("限流，或者降级了 fallback");
        return "限流，或者降级了 fallback";
    }

}
