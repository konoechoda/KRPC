package org.konoechoda.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.konoechoda.RpcApplication;
import org.konoechoda.model.RpcRequest;
import org.konoechoda.model.RpcResponse;
import org.konoechoda.register.LocalRegister;
import org.konoechoda.serializer.Serializer;
import org.konoechoda.serializer.SerializerFactory;


import java.io.IOException;
import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getSerializer(RpcApplication.getRpcConfig().getSerializer());
        // TODO 日志记录
        System.out.println("Received: " + httpServerRequest.method() + " " + httpServerRequest.uri());
        httpServerRequest.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null){
                rpcResponse.setMessage("rpcRequest is null");
                rpcResponse.setException(new Exception("rpcRequest is null"));
                doResponse(httpServerRequest, rpcResponse, serializer);
            }
            // 获取服务
            Class<?> imClass = LocalRegister.get(rpcRequest.getInterfaceName());
            try {
                Method method = imClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsTypes());
                Object result = method.invoke(imClass.newInstance(), rpcRequest.getParams());
                // 设置响应结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(result.getClass());
                rpcResponse.setMessage("success");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            doResponse(httpServerRequest, rpcResponse, serializer);
        });

    }

    public void doResponse(HttpServerRequest httpServerRequest, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse response = httpServerRequest.response().putHeader("content-type", "application/json");

        // 序列化响应结果
        byte[] bytes;
        try {
            bytes = serializer.serialize(rpcResponse);
            response.end(Buffer.buffer(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            response.end(Buffer.buffer());
        }

    }
}
