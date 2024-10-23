package org.konoechoda.server.impl;

import io.vertx.core.Vertx;
import org.konoechoda.server.HttpServer;
import org.konoechoda.server.HttpServerHandler;

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
                // TODO 日志记录
                System.out.println("Server started on port: " + port);
            } else {
                // TODO 日志记录
                System.out.println("Failed to bind: " + res.cause().getMessage());
            }
        });
    }
}
