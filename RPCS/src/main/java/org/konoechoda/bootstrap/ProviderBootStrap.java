package org.konoechoda.bootstrap;

import org.konoechoda.RpcApplication;
import org.konoechoda.config.RegisterConfig;
import org.konoechoda.config.RpcConfig;
import org.konoechoda.model.ServiceMetaInfo;
import org.konoechoda.model.ServiceRegisterInfo;
import org.konoechoda.register.LocalRegister;
import org.konoechoda.register.Register;
import org.konoechoda.register.RegisterFactory;
import org.konoechoda.server.ServiceProtocolKeys;
import org.konoechoda.server.http.VertxHttpServer;
import org.konoechoda.server.tcp.VertxTcpServer;

import java.util.List;
import java.util.Objects;

public class ProviderBootStrap {

    public static void init(List<ServiceRegisterInfo> serviceRegisterInfolist) {
        // rpc 框架初始化
        RpcApplication.init();
        // 服务注册
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfolist) {
            String serviceName = serviceRegisterInfo.getServiceName();
            LocalRegister.register(serviceName, serviceRegisterInfo.getImplClass());
            // 注册服务到注册中心
            RegisterConfig registerConfig = rpcConfig.getRegisterConfig();
            Register register = RegisterFactory.getInstance(registerConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getHost());
            serviceMetaInfo.setServicePort(rpcConfig.getPort());
            try {
                register.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " register error: ", e);
            }
        }
        // 启动服务
        startService(rpcConfig);
    }

    public static void startService(RpcConfig rpcConfig) {
        if (Objects.equals(rpcConfig.getProtocol(), ServiceProtocolKeys.TCP)) {
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.start(rpcConfig.getPort());
        }else if(Objects.equals(rpcConfig.getProtocol(), ServiceProtocolKeys.HTTP)){
            VertxHttpServer vertxHttpServer = new VertxHttpServer();
            vertxHttpServer.start(rpcConfig.getPort());
        }
    }

}
