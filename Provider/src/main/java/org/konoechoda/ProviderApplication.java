package org.konoechoda;

import org.konoechoda.bootstrap.ProviderBootStrap;
import org.konoechoda.model.ServiceRegisterInfo;
import org.konoechoda.register.LocalRegister;
import org.konoechoda.server.HttpServer;

import org.konoechoda.server.http.VertxHttpServer;
import org.konoechoda.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProviderApplication {
    public static void main(String[] args) throws IOException {
//        // 启动注册服务
//        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);
//        // 启动web服务
//        HttpServer httpServer = new VertxHttpServer();
//        httpServer.start(10010);
        List<ServiceRegisterInfo> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);
        // 启动服务
        ProviderBootStrap.init(serviceRegisterInfoList);
    }
}