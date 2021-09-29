package com.coisini.usercenter.rocketmq;

import com.coisini.usercenter.domain.entity.Test.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
//@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "add-test")
public class TestRocketConsumer implements RocketMQListener<Test> {
    @Override
    public void onMessage(Test test) {
        // TODO 业务处理
        try {
            log.info("监听到主题为'add-test'的消息：" + new ObjectMapper().writeValueAsString(test));
            log.info("可以开始处理业务啦啦啦");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
