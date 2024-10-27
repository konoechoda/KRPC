package org.konoechoda.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.konoechoda.RpcApplication;
import org.konoechoda.config.RpcConfig;
import org.konoechoda.constant.RpcConstant;
import org.konoechoda.fault.retry.RetryStrategy;
import org.konoechoda.fault.retry.RetryStrategyFactory;
import org.konoechoda.fault.tolerant.TolerantStrategy;
import org.konoechoda.fault.tolerant.TolerantStrategyFactory;
import org.konoechoda.loadbalancer.LoadBalancer;
import org.konoechoda.loadbalancer.LoadBalancerFactory;
import org.konoechoda.model.RpcRequest;
import org.konoechoda.model.RpcResponse;
import org.konoechoda.model.ServiceMetaInfo;
import org.konoechoda.protocol.*;
import org.konoechoda.register.Register;
import org.konoechoda.register.RegisterFactory;
import org.konoechoda.serializer.Serializer;
import org.konoechoda.serializer.SerializerFactory;
import org.konoechoda.server.tcp.VertxTcpClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 动态代理类
 */
public class TcpServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getSerializer(RpcApplication.getRpcConfig().getSerializer());
        String serviceName = method.getDeclaringClass().getName();
        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .paramsTypes(method.getParameterTypes())
                .params(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Register register = RegisterFactory.getInstance(rpcConfig.getRegisterConfig().getRegistry());
            ServiceMetaInfo metaInfo = new ServiceMetaInfo();
            metaInfo.setServiceName(serviceName);
            metaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = register.serviceDiscovery(metaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("service not found");
            }
            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 选择服务
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            // 发送TCP请求, 并启用重试策略
            RpcResponse rpcResponse;
            try{
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse =  retryStrategy.doRetry(() ->
                        VertxTcpClient.doRequest(rpcRequest, serviceMetaInfo)
                );
            }catch (Exception e){
                // 启用容错策略
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
                return tolerantStrategy.doTolerant(null, e);
            }

            return rpcResponse.getData();
//            Vertx vertx = Vertx.vertx();
//            NetClient client = vertx.createNetClient();
//            // 异步回调
//            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
//            // 连接
//            client.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(), result -> {
//                if (result.succeeded()) {
//                    System.out.println("TCP server connected");
//                    NetSocket socket = result.result();
//                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
//                    ProtocolMessage.Header header = protocolMessage.getHeader();
//                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
//                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
//                    header.setSerializer((byte) ProtocolMessageSerializerEnum.getByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
//                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
//                    header.setRequestId(IdUtil.getSnowflakeNextId());
//                    header.setLength(bodyBytes.length);
//                    protocolMessage.setHeader(header);
//                    protocolMessage.setBody(rpcRequest);
//                    //编码请求
//                    try {
//                        Buffer encoderBuffer = ProtocolMessageEncoder.encode(protocolMessage);
//                        socket.write(encoderBuffer);
//                    } catch (IOException e) {
//                        throw new RuntimeException("protocol message encode error");
//                    }
//                    //接受响应
//                    socket.handler(buffer -> {
//                        try {
//                            ProtocolMessage<RpcResponse> response = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
//                        } catch (IOException e) {
//                            throw new RuntimeException("protocol message decode error");
//                        }
//                    });
//                } else {
//                    System.out.println("TCP server connect failed");
//                }
//            });
//            RpcResponse rpcResponse = responseFuture.get();
//            client.close();
//            return rpcResponse.getData();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to encode request: " + e.getMessage());
        }
    }
}
