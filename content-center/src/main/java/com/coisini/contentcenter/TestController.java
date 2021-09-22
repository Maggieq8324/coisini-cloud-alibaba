package com.coisini.contentcenter;

import com.coisini.contentcenter.domain.dto.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;

    /**
     * 测试：服务发现，证明内容中心总能找到用户中心
     * @return 用户中心所有实例的地址信息
     */
    @GetMapping("test")
    public List<ServiceInstance> getInstances() {
        // 查询指定服务的所有实例的信息
        // consul/eureka/zookeeper...
        return discoveryClient.getInstances("user-center");
    }

    /**
     * 内容中心调用用户中心实例
     * @return
     */
    @GetMapping("test1")
    public String test1() {
        // 用户中心所有实例信息
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        String targetUrl = instances.stream()
                .map(instance -> instance.getUri().toString() + "/test/{name}")
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("当前没有实例"));

        log.info("目标地址：{}", targetUrl);

        RestTemplate restTemplate = new RestTemplate();
        // 调用用户微服务获取用户信息
        return restTemplate.getForObject(
                targetUrl,
                String.class,
                "Coisini"
        );
    }

    /**
     * 手写一个客户端侧负载均衡器
     * @return
     */
    @GetMapping("test2")
    public String test2() {
        // 用户中心所有实例信息
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        // 所有用户中心实例的请求地址
        List<String> targetUrls = instances.stream()
                 .map(instance -> instance.getUri().toString() + "/test/{name}")
                 .collect(Collectors.toList());

        // 使用随机算法随机选择实例
        int i = ThreadLocalRandom.current().nextInt(targetUrls.size());

        log.info("目标地址：{}", targetUrls.get(i));

        // 调用用户微服务获取用户信息
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                targetUrls.get(i),
                String.class,
                "Coisini"
        );
    }

    /**
     * 整合Ribbon
     * @return
     */
    @GetMapping("test3")
    public String test3() {
        /**
         * RestTemplate 请求时，ribbon会把user-center转换成用户中心在Nacos上的地址，
         * 进行负载均衡算法计算出一个实例去请求
         */
        return restTemplate.getForObject(
                "http://user-center/test/{name}",
                String.class,
                "Coisini"
        );
    }
}
