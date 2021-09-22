package com.coisini.usercenter.service.user;

import com.coisini.usercenter.dao.user.UserMapper;
import com.coisini.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description UserService
 * @author coisini
 * @date Sep 20, 2021
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;

    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
