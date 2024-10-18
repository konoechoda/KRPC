package org.kono.protocol;

import org.apache.commons.io.IOUtils;
import org.kono.common.Invocation;
import org.kono.register.LocalRegister;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServiceHandler {

    public void handle(HttpServletRequest req, HttpServletResponse resp) {
        // 处理请求 --> 方法调用
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();

            Class classImpl = LocalRegister.get(invocation.getInterfaceName(), invocation.getVersion());

            Method method = classImpl.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            Object result = method.invoke(classImpl.newInstance(), invocation.getParameters());

            IOUtils.write((String) result, resp.getOutputStream());
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
