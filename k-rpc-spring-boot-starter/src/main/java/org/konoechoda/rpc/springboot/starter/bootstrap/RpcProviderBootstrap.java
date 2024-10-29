package org.konoechoda.rpc.springboot.starter.bootstrap;


import lombok.extern.slf4j.Slf4j;
import org.konoechoda.RpcApplication;
import org.konoechoda.config.RegisterConfig;
import org.konoechoda.config.RpcConfig;
import org.konoechoda.model.ServiceMetaInfo;
import org.konoechoda.register.LocalRegister;
import org.konoechoda.register.Register;
import org.konoechoda.register.RegisterFactory;
import org.konoechoda.rpc.springboot.starter.annotation.RpcService;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Rpc服务提供者启动类
 */
@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {
    /**
     * 在bean初始化之前，为带有RpcService注解的类注册服务
     *
     * @param bean bean对象
     * @param beanName bean名称
     * @return bean对象
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if(rpcService != null){
            //获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            //默认值处理
            if (interfaceClass == void.class){
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();
            //注册服务
            LocalRegister.register(serviceName, beanClass);
            //全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            // 注册服务到注册中心
            RegisterConfig registryConfig = rpcConfig.getRegisterConfig();
            Register register = RegisterFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getHost());
            serviceMetaInfo.setServicePort(rpcConfig.getPort());
            try {
                register.register(serviceMetaInfo);
            }catch ( Exception e){
                throw new RuntimeException(serviceName +"服务注册失败"+e);
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

}