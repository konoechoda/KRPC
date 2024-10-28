package org.konoechoda.proxy;

import org.konoechoda.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * 动态代理工厂类
 */
public class ServiceProxyFactory {
    /**
     * 创建代理对象 - http
     * @param interfaceClass 接口
     * @param <T> 泛型
     * @return 代理对象
     */
    public static <T> T getProxy(Class<T> interfaceClass) {
        if(RpcApplication.getRpcConfig().getMock()) {
            return getMockProxy(interfaceClass);
        }

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new ServiceProxy()
        );
    }

    /**
     * 创建代理对象 - tcp
     * @param interfaceClass 接口
     * @param <T> 泛型
     * @return 代理对象
     */
    public static <T> T getTcpProxy(Class<T> interfaceClass) {
        if(RpcApplication.getRpcConfig().getMock()) {
            return getMockProxy(interfaceClass);
        }

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new TcpServiceProxy()
        );
    }

    /**
     * 创建模拟代理对象
     * @param interfaceClass 接口
     * @param <T> 泛型
     * @return 代理对象
     */
    public static <T> T getMockProxy(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new ServiceMockProxy()
        );
    }
}
