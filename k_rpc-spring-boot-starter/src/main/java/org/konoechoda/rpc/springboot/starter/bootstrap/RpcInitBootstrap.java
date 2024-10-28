package org.konoechoda.rpc.springboot.starter.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.konoechoda.RpcApplication;
import org.konoechoda.config.RpcConfig;
import org.konoechoda.rpc.springboot.starter.annotation.EnableRpc;
import org.konoechoda.server.tcp.VertxTcpServer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * RPC框架初始化
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    /**
     * RPC框架初始化
     *
     * @param importingClassMetadata 注解元数据
     * @param registry BeanDefinitionRegistry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //获取EnableRpc注解信息
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");
        //RPC框架初始化
        RpcApplication.init();
        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        //启动服务器
        if (needServer) {
            VertxTcpServer tcpServer = new VertxTcpServer();
            tcpServer.start(rpcConfig.getPort());
        }else{
            log.info("RPC Server is not started");
        }
    }
}