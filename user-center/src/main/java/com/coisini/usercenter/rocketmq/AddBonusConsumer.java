package com.coisini.usercenter.rocketmq;

import com.coisini.usercenter.dao.bonus.BonusEventLogMapper;
import com.coisini.usercenter.dao.user.UserMapper;
import com.coisini.usercenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.coisini.usercenter.domain.entity.bonus.BonusEventLog;
import com.coisini.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "add-bonus")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusConsumer implements RocketMQListener<UserAddBonusMsgDTO> {

    private final UserMapper userMapper;
    private final BonusEventLogMapper bonusEventLogMapper;

    @Override
    public void onMessage(UserAddBonusMsgDTO message) {
        // 当收到消息时执行的业务

        // 为用户加积分
        Integer userId = message.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus() + message.getBonus());
        userMapper.updateByPrimaryKeySelective(user);

        Integer bonus = user.getBonus();

        // 记录日志到bonus_event_log里
        bonusEventLogMapper.insert(
                BonusEventLog.builder()
                    .userId(userId)
                    .value(bonus)
                    .event("CONTRIBUTE")
                    .createTime(new Date())
                    .description("投稿加积分")
                    .build()
        );

        log.info("积分添加完毕。。。");
    }
}
