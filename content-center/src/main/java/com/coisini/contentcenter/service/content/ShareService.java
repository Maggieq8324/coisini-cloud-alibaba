package com.coisini.contentcenter.service.content;

import com.coisini.contentcenter.dao.content.ShareMapper;
import com.coisini.contentcenter.dao.messaging.RocketmqTransactionLogMapper;
import com.coisini.contentcenter.domain.dto.content.ShareAuditDTO;
import com.coisini.contentcenter.domain.dto.content.ShareDTO;
import com.coisini.contentcenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.coisini.contentcenter.domain.dto.user.UserDTO;
import com.coisini.contentcenter.domain.entity.content.Share;
import com.coisini.contentcenter.domain.entity.messaging.RocketmqTransactionLog;
import com.coisini.contentcenter.domain.enums.AuditStatusEnum;
import com.coisini.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @Description ShareService
 * @author coisini
 * @date Sep 20, 2021
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {

    private final ShareMapper shareMapper;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;
    private final UserCenterFeignClient userCenterFeignClient;
    private final RocketMQTemplate rocketMQTemplate;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    public ShareDTO findById(Integer id) {
        // 获取分享详情
        Share share = shareMapper.selectByPrimaryKey(id);

        Integer userId = share.getUserId();

//        // 用户中心所有实例信息
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        // 所有用户中心实例的请求地址
//        List<String> targetUrls = instances.stream()
//                 .map(instance -> instance.getUri().toString() + "/users/{id}")
//                 .collect(Collectors.toList());
//
//        // 使用随机算法随机选择实例
//        int i = ThreadLocalRandom.current().nextInt(targetUrls.size());
//
//        log.info("目标地址：{}", targetUrls.get(i));
//
//        // 调用用户微服务获取用户信息
//        UserDTO userDTO = restTemplate.getForObject(
//                targetUrls.get(i),
//                UserDTO.class,
//                userId
//        );

        // RestTemplate 请求时，ribbon会把user-center转换成用户中心在Nacos上的地址，
        // 进行负载均衡算法计算出一个实例去请求
//        UserDTO userDTO = restTemplate.getForObject(
//                "http://user-center/users/{userId}",
//                UserDTO.class,
//                userId
//        );

        // Feign 调用
        UserDTO userDTO = userCenterFeignClient.findById(userId);

        // 消息装配
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

    /**
     * 管理员审核分享
     * @param id
     * @param auditDTO
     * @return
     */
    public Share auditById(Integer id, ShareAuditDTO auditDTO) {
        // 1. 查询share是否存在，不存在或者当前的audit_status != NOT_YET，那么抛异常
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法！该分享不存在！");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法！该分享已审核通过或审核不通过！");
        }

        String transactionId = UUID.randomUUID().toString();

        // 3. 如果是PASS，那么发送消息给rocketmq，让用户中心去消费，并为发布人添加积分
        if (AuditStatusEnum.PASS.equals(auditDTO.getAuditStatusEnum())) {
            // 发送半消息
            rocketMQTemplate.sendMessageInTransaction(
                    "add-bonus",
                    MessageBuilder.withPayload(
                        UserAddBonusMsgDTO.builder()
                                .userId(share.getUserId())
                                .bonus(50)
                                .build()
                    ).setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                     .setHeader("share_id", id)
                     .build(),
                    auditDTO
            );
        } else {
            auditByIdInDB(id, auditDTO);
        }

        return share;
    }

    /**
     * 审核资源
     */
    @Transactional(rollbackFor = Exception.class)
    public void auditByIdInDB(Integer id, ShareAuditDTO auditDTO) {
        Share share = Share.builder()
                .id(id)
                .auditStatus(auditDTO.getAuditStatusEnum().toString())
                .reason(auditDTO.getReason())
                .build();
        shareMapper.updateByPrimaryKeySelective(share);
    }

    /**
     * 审核资源且记录事务日志
     */
    @Transactional(rollbackFor = Exception.class)
    public void auditByIdWithRocketMqLog(Integer id, ShareAuditDTO auditDTO, String transactionId) {
        this.auditByIdInDB(id, auditDTO);

        rocketmqTransactionLogMapper.insertSelective(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .log("审核分享...")
                        .build()
        );
    }


}
