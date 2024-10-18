package org.kono.proxy;

import org.kono.common.Invocation;
import org.kono.common.URL;
import org.kono.loadBalance.LoadBalance;
import org.kono.protocol.HttpClient;
import org.kono.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass, String version){

        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Invocation invocation =
                        new Invocation(interfaceClass.getName(), method.getName(), method.getParameterTypes(), args, version);

                List<URL> list = MapRemoteRegister.get(interfaceClass.getName());

                URL url = LoadBalance.random(list);

                String result = new HttpClient().send(url.getHostname(), url.getPort(), invocation);

                return result;
            }
        });

        return (T) proxyInstance;
    }
}
