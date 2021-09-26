package com.coisini.contentcenter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

/**
 * @Description Sentinel使用示例
 *      https://blog.csdn.net/weixin_41182727/article/details/120462016
 *          SphU 定义资源，让资源受到监控且可以保护资源
 *          Tracer 可以对想要的异常进行统计
 *          ContextUtil 可以实现调用来源，还可以标记调用
 *
 * @author coisini
 * @date
 * @Version 1.0
 */
@RestController
@RequestMapping("/sentinel")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestSentinelController {

    /**
     * 用SphU.entry定义一个资源后
     * Sentinel会对该资源进行监控，计算它的QPS，线程数，RT，错误率等
     * 如果超如阈值就抛出BlockException
     * @param name
     * @return
     */
    @GetMapping("/sentinel_api_test")
    public String testSentinelAPI(@RequestParam(required = false) String name) {
        String resourceName = "sentinel_api_test";

        // 定义一个sentinel保护的资源，名称是test-sentinel-api
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            return name;
        }
        // 如果被保护的资源被限流或者降级了，就会抛BlockException
        catch (BlockException e) {
            log.warn("限流，或者降级了", e);
            return "限流，或者降级了";
        } finally {
            if (Objects.nonNull(entry)) {
                // 退出entry
                entry.exit();
            }
        }
    }

    /**
     * Tracer.trace()统计BlockException以外的异常
     * 默认情况下，Sentinel只会统计BlockException及其子类
     * @param name
     * @return
     */
    @GetMapping("/sentinel_api_test1")
    public String testSentinelAPI1(@RequestParam(required = false) String name) {
        String resourceName = "sentinel_api_test1";

        // 定义一个sentinel保护的资源，名称是sentinel_api_test1
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            // 被保护的业务逻辑
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name不能为空");
            }
            return name;
        }
        // 如果被保护的资源被限流或者降级了，就会抛BlockException
        catch (BlockException e) {
            log.warn("限流，或者降级了", e);
            return "限流，或者降级了";
        } catch (IllegalArgumentException e2) {
            // 统计IllegalArgumentException 发生的次数、发生占比...
            Tracer.trace(e2);
            return "非法参数";
        } finally {
            if (Objects.nonNull(entry)) {
                // 退出entry
                entry.exit();
            }
        }
    }


    /**
     * ContextUtil.enter()设置针对来源
     * @param name
     * @return
     */
    @GetMapping("/sentinel_api_test2")
    public String testSentinelAPI2(@RequestParam(required = false) String name) {
        String resourceName = "sentinel_api_test2";

        // ContextUtil.enter()设置来源名为test-server的微服务
        ContextUtil.enter(resourceName, "test-server");

        // 定义一个sentinel保护的资源，名称是test-sentinel-api2
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            // 被保护的业务逻辑
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name不能为空");
            }
            return name;
        }
        // 如果被保护的资源被限流或者降级了，就会抛BlockException
        catch (BlockException e) {
            log.warn("限流，或者降级了", e);
            return "限流，或者降级了";
        } catch (IllegalArgumentException e2) {
            // 统计IllegalArgumentException 发生的次数、发生占比...
            Tracer.trace(e2);
            return "非法参数";
        } finally {
            if (Objects.nonNull(entry)) {
                // 退出entry
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

//    @GetMapping("/test-sentinel-resource")
//    @SentinelResource(
//            value = "test-sentinel-api",
//            blockHandler = "block",
//            fallback = "fallback"
//    )
//    public String testSentinelResource(@RequestParam(required = false) String a) {
//        if (StringUtils.isBlank(a)) {
//            throw new IllegalArgumentException("a cannot be blank.");
//        }
//        return a;
//    }
//
//    public String block(String a, BlockException e) {
//        log.warn("限流，或者降级了", e);
//        return "限流，或者降级了 block";
//    }
//
//    public String fallback(String a) {
//        return "限流，或者降级了 fallback";
//    }

}
