package ribbonconfiguration;

import com.coisini.contentcenter.configuration.NacosSameClusterWeightedRule;
import com.coisini.contentcenter.configuration.NacosWeightedRule;
//import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
//import com.netflix.loadbalancer.PingUrl;
//import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description Ribbon的配置类
 *      Ribbon的配置类要有@Configuration注解，但不能被@ComponentScan重复扫描
 *      否则就会被所有的RibbonClient共享
 *      避免父子上下文重叠
 * @author coisini
 * @date Sep 22, 2021
 * @Version 1.0
 */
@Configuration
public class RibbonConfiguration {
    /**
     * 自定义负载均衡规则
     * RandomRule 随机选择
     * @return
     */
    @Bean
    public IRule ribbonRule() {
        return new NacosSameClusterWeightedRule();
    }

    /**
     * 自定义Iping 筛选 Ping不通的实例
     * @return
     */
//    @Bean
//    public IPing ping() {
//        return new PingUrl();
//    }
}

