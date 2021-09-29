package com.coisini.contentcenter.rocketmq;

import com.coisini.contentcenter.dao.messaging.RocketmqTransactionLogMapper;
import com.coisini.contentcenter.domain.entity.messaging.RocketmqTransactionLog;
import com.coisini.contentcenter.domain.entity.test.Test;
import com.coisini.contentcenter.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import java.util.Objects;

/**
 * 分布式事务消息
 * 事务监听
 */
@RocketMQTransactionListener(txProducerGroup = "add-test-group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddTestTransactionListener implements RocketMQLocalTransactionListener {

    private final TestService testService;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    /**
     * 执行本地事务，对应步骤三
     * @param message
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {

        MessageHeaders headers = message.getHeaders();

        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);

        try {
            testService.insertTestDataWithRocketMqLog((Test) o, transactionId);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 本地事务回查，对应步骤六
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);

        // 根据记录的事务回查
        RocketmqTransactionLog transactionLog = rocketmqTransactionLogMapper.selectOne(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .build()
        );

        // 本地事务执行成功
        if (Objects.nonNull(transactionLog)) {
            return RocketMQLocalTransactionState.COMMIT;
        }

        // 本地事务执行失败
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
