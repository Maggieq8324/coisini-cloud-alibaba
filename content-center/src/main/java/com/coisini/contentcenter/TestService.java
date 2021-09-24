package com.coisini.contentcenter;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @SentinelResource("common")
    public void common() {
        System.out.println("common");
    }

}
