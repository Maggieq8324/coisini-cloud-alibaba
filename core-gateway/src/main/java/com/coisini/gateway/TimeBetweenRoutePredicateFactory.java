package com.coisini.gateway;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义路由谓词工厂
 * 路由谓词工厂必须以RoutePredicateFactory结尾
 */
@Component
public class TimeBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeBetweenConfig> {

    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetweenConfig.class);
    }

    /**
     * 控制路由的条件
     * @param config
     * @return
     */
    @Override
    public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
        LocalTime start = config.getStart();
        LocalTime end = config.getEnd();

//        return new Predicate<ServerWebExchange>() {
//            @Override
//            public boolean test(ServerWebExchange exchange) {
//                LocalTime now = LocalTime.now();
//                return now.isAfter(start) && now.isBefore(end);
//            }
//        };

        return exchange -> {
            LocalTime now = LocalTime.now();
            Boolean flag = now.isAfter(start) && now.isBefore(end);
            System.out.println("start===========" + start);
            System.out.println("end===========" + end);
            System.out.println("now===========" + now);
            System.out.println("now.isAfter(start)===========" + now.isAfter(start));
            System.out.println("now.isBefore(end)===========" + now.isBefore(end));
            System.out.println("flag===========" + flag);
            return now.isAfter(start) && now.isBefore(end);
        };
    }

    /**
     * 控制配置类和配置文件的映射关系
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("start", "end");
    }

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        System.out.println(formatter.format(LocalTime.now()));
    }
}
