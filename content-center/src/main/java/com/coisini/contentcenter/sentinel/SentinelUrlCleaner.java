package com.coisini.contentcenter.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * @Description Sentinel Restful URL支持
 * @author coisini
 * @date Sep 27， 2021
 * @Version 1.0
 */
@Component
@Slf4j
public class SentinelUrlCleaner implements UrlCleaner {
    @Override
    public String clean(String originUrl) {
        // 让 /shares/1 与 /shares/2 的返回值相同
        // 返回/shares/{number}

        String[] split = originUrl.split("/");

        return Arrays.stream(split)
                .map(string -> {
                    if (NumberUtils.isNumber(string)) {
                        return "{number}";
                    }
                    return string;
                })
                .reduce((a, b) -> a + "/" + b)
                .orElse("");
    }
}

