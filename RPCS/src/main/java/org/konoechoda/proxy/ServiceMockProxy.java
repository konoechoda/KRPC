package org.konoechoda.proxy;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class ServiceMockProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 根据返回值类型，生成特定的返回值对象
        Class<?> returnType = method.getReturnType();
        log.info("mock proxy invoke method: {}", returnType.getName());
        return getDefaultObject(returnType);
    }

    private Object getDefaultObject(Class<?> returnType) {
        // 基本类型
        if (returnType.isPrimitive()) {
            if (returnType == int.class) {
                return 0;
            } else if (returnType == long.class) {
                return 0L;
            } else if (returnType == float.class) {
                return 0.0f;
            } else if (returnType == double.class) {
                return 0.0d;
            } else if (returnType == boolean.class) {
                return false;
            } else if (returnType == char.class) {
                return '\u0000';
            } else if (returnType == byte.class) {
                return (byte) 0;
            } else if (returnType == short.class) {
                return (short) 0;
            }
        }
        // 对象类型
        return null;
    }
}
