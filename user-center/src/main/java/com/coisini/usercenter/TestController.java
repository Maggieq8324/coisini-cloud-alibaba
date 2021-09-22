package com.coisini.usercenter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/test/{name}")
    public String test(@PathVariable String name) {
        log.info("请求...");
        return "hello " + name;
    }

}
