package com.coisini.contentcenter.service.test;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @SentinelResource("common")
    public void common() {
        System.out.println("common");
    }

}
