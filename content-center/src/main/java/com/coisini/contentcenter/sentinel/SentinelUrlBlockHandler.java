package com.coisini.contentcenter.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.coisini.contentcenter.model.UnifyMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description Sentinel 自定义异常返回
 *      此代码在1.8.1版本无效。。。未处理
 * @author coisini
 * @date Sep 27, 2021
 * @Version 1.0
 */
@Component
public class SentinelUrlBlockHandler implements UrlBlockHandler {

    @Override
    public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
        UnifyMessage msg = null;
        if (e instanceof FlowException) {
            // 限流异常
            msg = UnifyMessage.builder()
                    .status(100)
                    .msg("限流了")
                    .build();
        } else if (e instanceof DegradeException) {
            // 降级异常
            msg = UnifyMessage.builder()
                    .status(101)
                    .msg("降级了")
                    .build();
        } else if (e instanceof ParamFlowException) {
            // 参数热点规则异常
            msg = UnifyMessage.builder()
                    .status(102)
                    .msg("热点参数限流")
                    .build();
        } else if (e instanceof SystemBlockException) {
            // 系统规则异常
            msg = UnifyMessage.builder()
                    .status(103)
                    .msg("系统规则（负载/...不满足要求）")
                    .build();
        } else if (e instanceof AuthorityException) {
            // 授权异常
            msg = UnifyMessage.builder()
                    .status(104)
                    .msg("授权规则不通过")
                    .build();
        }

        response.setStatus(500);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(response.getWriter(), msg);
    }
}


