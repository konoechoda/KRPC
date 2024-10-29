package org.konoechoda.rpc.springboot.starter.bootstrap;

import org.konoechoda.proxy.ServiceProxyFactory;
import org.konoechoda.rpc.springboot.starter.annotation.RpcReference;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Rpc消费者启动类
 */
public class RpcConsumerBootstrap implements BeanPostProcessor {
    /**
     * 在bean初始化之前，为带有RpcReference注解的字段生成代理对象
     *
     * @param bean bean对象
     * @param beanName bean名称
     * @return bean对象
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        //遍历对象的所有字段
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                // 为属性生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);
                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass);
                try {
                    field.set(bean, proxyObject);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为字段注入代理对象失败", e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}