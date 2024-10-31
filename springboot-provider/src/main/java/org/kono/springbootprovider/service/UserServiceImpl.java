package org.kono.springbootprovider.service;

import org.konoechoda.model.User;
import org.konoechoda.rpc.springboot.starter.annotation.RpcService;
import org.konoechoda.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        user.setName(user.getName() + "from sb");
        return user;
    }
}
