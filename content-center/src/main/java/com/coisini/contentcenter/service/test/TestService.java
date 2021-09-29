package com.coisini.contentcenter.service.test;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.coisini.contentcenter.dao.messaging.RocketmqTransactionLogMapper;
import com.coisini.contentcenter.dao.test.TestMapper;
import com.coisini.contentcenter.domain.entity.messaging.RocketmqTransactionLog;
import com.coisini.contentcenter.domain.entity.test.Test;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestService {

    /**
     * RocketMQ分布式事务
     * 流程图：https://img-blog.csdnimg.cn/25ebe5016a67418aad8a45aca3816007.png
     *  1、生产者向MQ Server发送半消息（特殊消息，会被存储到MQ Server且标记为暂时不能投递），
     *      消费者不会接收到这条消息
     * 2 3、当半消息发送成功后生产者就去执行本地事务
     * 4、生产者根据本地事务的执行状态向MQ Server发送二次确认请求，
     *      如果MQ Server收到的是commit就将半消息标记为可投递，消费者即可消费到该消息，
     *      如果接收到是rollback就将这条半消息删除
     * 5、如果第四步的二次确认没有能够成功发送到MQ Server，经过一段时间后，
     *      MQ Server会向生产者发送回查消息去获取本地事务的执行状态
     * 6、生产者检查本地事务执行状态
     * 7、生产者根据本地事务的执行结果告诉MQ Server应该commit还是rollback，
     *      如果是commit则像消费者投递消息，如果是rollback则丢弃消息
     */

    @SentinelResource("common")
    public void common() {
        System.out.println("common");
    }

    private final TestMapper testMapper;
    private final RocketMQTemplate rocketMQTemplate;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    public Test insertTest() {
        Test test = Test.builder()
                .title("世事短如春梦，春梦了无痕，譬如春梦，黄粱未熟蕉鹿走")
                .build();

        /**
         * 发送半消息 对应步骤一
         * 参数1：Topic
         * 参数2：消息体
         *      可设置header，可用作参数传递
         * 参数2：arg 可用作参数传递
         */
        rocketMQTemplate.sendMessageInTransaction(
                "add-test-gropu",
                "add-test",
                MessageBuilder.withPayload(test)
                              .setHeader(RocketMQHeaders.TRANSACTION_ID, UUID.randomUUID().toString())
                              .build(),
                 test
        );

        return test;
    }

    /**
     * 插入数据且记录事务日志
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertTestDataWithRocketMqLog(Test test, String transactionId) {
        this.insertTestData(test);

        rocketmqTransactionLogMapper.insertSelective(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .log("插入了一条Test数据...")
                        .build()
        );
    }

    /**
     * 插入测试数据
     * @param test
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertTestData(Test test) {
        testMapper.insertSelective(test);
    }


}
