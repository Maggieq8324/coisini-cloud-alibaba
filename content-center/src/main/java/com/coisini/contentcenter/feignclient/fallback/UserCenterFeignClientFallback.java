package com.coisini.contentcenter.feignclient.fallback;

import com.coisini.contentcenter.domain.dto.user.UserDTO;
import com.coisini.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * UserCenterFeignClient Sentinel 的远程调用业务逻辑处理
 */
@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {
    @Override
    public UserDTO findById(Integer id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setWxNickname("流控/降级返回的用户");
        return userDTO;
    }
}

