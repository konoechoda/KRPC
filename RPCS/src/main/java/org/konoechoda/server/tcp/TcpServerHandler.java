package org.konoechoda.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import org.konoechoda.model.RpcRequest;
import org.konoechoda.model.RpcResponse;
import org.konoechoda.protocol.ProtocolMessage;
import org.konoechoda.protocol.ProtocolMessageDecoder;
import org.konoechoda.protocol.ProtocolMessageEncoder;
import org.konoechoda.protocol.ProtocolMessageTypeEnum;
import org.konoechoda.register.LocalRegister;

import java.lang.reflect.Method;

public class TcpServerHandler implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket netSocket) {
        // 处理情趣
        TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            // 接收请求, 解码
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (Exception e) {
                throw new RuntimeException("Decode error", e);
            }
            RpcRequest rpcRequest = protocolMessage.getBody();
            // 处理请求
            RpcResponse rpcResponse = new RpcResponse();
            try {
                // 获取要调用的服务, 通过反射调用
                Class<?> implClass = LocalRegister.get(rpcRequest.getInterfaceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getParams());
                // 封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                rpcResponse.setMessage("error");
                rpcResponse.setException(e);
                e.printStackTrace();
            }

            // 返回结果, 编码
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> responseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
            try {
                Buffer encode = ProtocolMessageEncoder.encode(responseProtocolMessage);
                netSocket.write(encode);
            } catch (Exception e) {
                throw new RuntimeException("Encode error", e);
            }

        });
        netSocket.handler(tcpBufferHandlerWrapper);
    }
}
