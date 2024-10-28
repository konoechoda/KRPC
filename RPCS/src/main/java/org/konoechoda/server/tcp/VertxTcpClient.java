package org.konoechoda.server.tcp;

import cn.hutool.core.util.IdUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;
import org.konoechoda.RpcApplication;
import org.konoechoda.model.RpcRequest;
import org.konoechoda.model.RpcResponse;
import org.konoechoda.model.ServiceMetaInfo;
import org.konoechoda.protocol.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Vertx TCP 客户端
 */
@Slf4j
public class VertxTcpClient {


    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        // 发送 TCP 请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();

        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(), result -> {
            if (!result.succeeded()) {
                log.error("Failed to connect to server: {}", result.cause().getMessage());
                return;
            }
            log.info("Connected to server: {}:{}", serviceMetaInfo.getServiceHost(), serviceMetaInfo.getServicePort());
            NetSocket socket = result.result();
            // 发送数据
            // 构造消息
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            header.setRequestId(IdUtil.getSnowflakeNextId());
            protocolMessage.setHeader(header);
            protocolMessage.setBody(rpcRequest);
            // 编码请求
            try {
                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encodeBuffer);
            } catch (Exception e) {
                throw new RuntimeException("Failed to encode request: " + e.getMessage());
            }

            // 接收响应
            TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                try {
                    ProtocolMessage<RpcResponse> responseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                    responseFuture.complete(responseProtocolMessage.getBody());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode response: " + e.getMessage());
                }
            });
            socket.handler(bufferHandlerWrapper);

        });
        RpcResponse rpcResponse = responseFuture.get();
        netClient.close();
        return rpcResponse;
    }

//
//    public void start() {
//        // 创建 vertx 实例
//        Vertx vertx = Vertx.vertx();
//        // 创建 TCP 客户端
//        vertx.createNetClient().connect(10010, "localhost", res -> {
//            if (res.succeeded()) {
//                log.info("Connected!");
//                NetSocket socket = res.result();
//                // 发送数据
//                socket.write("Hello, server");
//                // 接收数据
//                socket.handler(buffer -> {
//                    log.info("Received: {}", buffer.getString(0, buffer.length()));
//                });
//            } else {
//                log.error("Failed to connect: {}", res.cause().getMessage());
//            }
//        });
//
//    }

}
