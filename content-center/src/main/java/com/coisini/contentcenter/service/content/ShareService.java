package com.coisini.contentcenter.service.content;

import com.coisini.contentcenter.dao.content.ShareMapper;
import com.coisini.contentcenter.domain.dto.content.ShareDTO;
import com.coisini.contentcenter.domain.dto.user.UserDTO;
import com.coisini.contentcenter.domain.entity.content.Share;
import com.coisini.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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

    public static void main(String[] args) {

    }

}
