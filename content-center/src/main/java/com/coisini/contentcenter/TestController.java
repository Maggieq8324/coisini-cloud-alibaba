package com.coisini.contentcenter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.coisini.contentcenter.feignclient.TestBaiduFeignClient;
import com.coisini.contentcenter.feignclient.TestFeignClient;
import com.coisini.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final TestFeignClient testFeignClient;
    private final TestBaiduFeignClient testBaiduFeignClient;

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

    /**
     * 整合Feign
     * @return
     */
    @GetMapping("test4")
    public String test4() {
        return testFeignClient.test("Coisini");
    }

    /**
     * feign脱离ribbon的使用
     * @return
     */
    @GetMapping("baidu")
    public String baiduIndex() {
        return testBaiduFeignClient.index();
    }

    @Autowired
    private TestService testService;

    /**
     * Sentinel 流控链路模式
     * @return
     */
    @GetMapping("test_a")
    public String test_a() {
        testService.common();
        return "test_a";
    }

    @GetMapping("test_b")
    public String test_b() {
        testService.common();
        return "test_b";
    }

    @GetMapping("/test-sentinel-api")
    public String testSentinelAPI(@RequestParam(required = false) String a) {
        String resourceName = "test-sentinel-api";
        ContextUtil.enter(resourceName, "test-wfw");

        // 定义一个sentinel保护的资源，名称是test-sentinel-api
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            // 被保护的业务逻辑
            if (StringUtils.isBlank(a)) {
                throw new IllegalArgumentException("a不能为空");
            }
            return a;
        }
        // 如果被保护的资源被限流或者降级了，就会抛BlockException
        catch (BlockException e) {
            log.warn("限流，或者降级了", e);
            return "限流，或者降级了";
        } catch (IllegalArgumentException e2) {
            // 统计IllegalArgumentException【发生的次数、发生占比...】
            Tracer.trace(e2);
            return "参数非法！";
        } finally {
            if (entry != null) {
                // 退出entry
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    @GetMapping("/test-sentinel-resource")
    @SentinelResource(
            value = "test-sentinel-api",
            blockHandler = "block",
            fallback = "fallback"
    )
    public String testSentinelResource(@RequestParam(required = false) String a) {
        if (StringUtils.isBlank(a)) {
            throw new IllegalArgumentException("a cannot be blank.");
        }
        return a;
    }

    public String block(String a, BlockException e) {
        log.warn("限流，或者降级了", e);
        return "限流，或者降级了 block";
    }

    public String fallback(String a) {
        return "限流，或者降级了 fallback";
    }

}
