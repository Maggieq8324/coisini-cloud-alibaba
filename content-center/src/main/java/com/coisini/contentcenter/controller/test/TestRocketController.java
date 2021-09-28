package com.coisini.contentcenter.controller.test;

import com.coisini.contentcenter.domain.entity.test.Test;
import com.coisini.contentcenter.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rocketmq")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestRocketController {

    private final RocketMQTemplate rocketMQTemplate;
    private final TestService testService;

    @PostMapping("test")
    public String test() {
        rocketMQTemplate.convertAndSend(
                "test-msg",
                "突然活得潦草了。。。");

        return "success";
    }

    @PostMapping("test1")
    public Test test1() {
        return testService.insertTest();
    }

}
