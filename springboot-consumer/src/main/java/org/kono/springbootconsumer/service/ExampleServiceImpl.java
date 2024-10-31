package org.kono.springbootconsumer.service;

import org.konoechoda.model.User;
import org.konoechoda.rpc.springboot.starter.annotation.RpcReference;
import org.konoechoda.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {
    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("hpq");
        User result = userService.getUser(user);
        System.out.println(result.getName());
    }
}
