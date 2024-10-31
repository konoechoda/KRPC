package org.kono.springbootconsumer.controller;

import org.kono.springbootconsumer.service.ExampleServiceImpl;
import org.konoechoda.model.User;
import org.konoechoda.rpc.springboot.starter.annotation.RpcReference;
import org.konoechoda.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private ExampleServiceImpl userService;

    @RequestMapping("/test")
    public String testUserService(){
        userService.test();
        return "ok";
    }
}
