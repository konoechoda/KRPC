package org.konoechoda.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

/**
 * Vertx TCP 客户端
 */
@Slf4j
public class VertxTcpClient {

    public void start() {
        // 创建 vertx 实例
        Vertx vertx = Vertx.vertx();
        // 创建 TCP 客户端
        vertx.createNetClient().connect(10010, "localhost", res -> {
            if (res.succeeded()) {
                log.info("Connected!");
                NetSocket socket = res.result();
                // 发送数据
                socket.write("Hello, server");
                // 接收数据
                socket.handler(buffer -> {
                    log.info("Received: {}", buffer.getString(0, buffer.length()));
                });
            } else {
                log.error("Failed to connect: {}", res.cause().getMessage());
            }
        });

    }

}
