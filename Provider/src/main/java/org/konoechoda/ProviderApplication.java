package org.konoechoda;

import org.konoechoda.register.LocalRegister;
import org.konoechoda.server.HttpServer;
import org.konoechoda.server.impl.VertxHttpServerImpl;
import org.konoechoda.service.UserService;

import java.io.IOException;

public class ProviderApplication {
    public static void main(String[] args) throws IOException {
        // 启动注册服务
        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);
        // 启动web服务
        HttpServer httpServer = new VertxHttpServerImpl();
        httpServer.start(10010);
    }
}