package org.konoechoda.server.tcp;


import com.google.common.annotations.VisibleForTesting;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;
import org.konoechoda.server.HttpServer;

/**
 * Vert.x TCP 服务
 */
@Slf4j
public class VertxTcpServer implements HttpServer {

    private byte[] handlerRequest(byte[] request) {
        return "Hello, client".getBytes();
    }

    /**
     * 启动服务
     *
     * @param port 端口
     */
    @Override
    public void start(int port) {
        // 创建 vertx 实例
        Vertx vertx = Vertx.vertx();
        // 创建 TCP 服务
        NetServer server = vertx.createNetServer();
        // 处理请求
        server.connectHandler(socket -> {
            // 接收数据
            socket.handler(buffer -> {
                byte[] request = buffer.getBytes();
                // 处理请求
                byte[] response = handlerRequest(request);
                // 响应数据
                socket.write(Buffer.buffer(response));
            });
        });
        // 监听端口
        server.listen(port, res -> {
            if (res.succeeded()) {
                log.info("TCP Server started on port {}", port);
            } else {
                log.error("TCP Server failed to start on port {}: {}", port, res.cause().getMessage());
                throw new RuntimeException("TCP Server failed to start: " + res.cause().getMessage());
            }
        });

    }

}
