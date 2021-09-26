package com.coisini.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestBlockHandler {
    /**
     * 处理限流或者降级
     * @param name
     * @param e
     * @return
     */
    public static String block(String name, BlockException e) {
        log.warn("限流，或者降级了 block", e);
        return "限流，或者降级了 block";
    }
}
