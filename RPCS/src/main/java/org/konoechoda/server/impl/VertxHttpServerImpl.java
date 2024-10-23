package org.konoechoda.server.impl;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.konoechoda.server.HttpServer;
import org.konoechoda.server.HttpServerHandler;

@Slf4j
public class VertxHttpServerImpl implements HttpServer {

    @Override
    public void start(int port) {
        // 创建Vertx实例
        Vertx vertx = Vertx.vertx();
        // 创建HttpServer实例
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 处理请求
//        server.requestHandler(request -> {
//            // 处理http请求
//            System.out.println("Received: " + request.method() + " " + request.uri());
//            // 响应
//            request.response().putHeader("content-type", "text/plain").end("Hello World from Vert.x");
//        });
        server.requestHandler(new HttpServerHandler());

        // 监听端口
        server.listen(port, res -> {
            if (res.succeeded()) {
                log.info("Server started on port: {}", port);
            } else {
                log.error("Failed to bind: {}", res.cause().getMessage());
            }
        });
    }
}
