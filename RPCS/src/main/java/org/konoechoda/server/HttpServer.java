package org.konoechoda.server;

/**
 * Http服务
 */
public interface HttpServer {

    /**
     * 启动服务
     * @param port 端口
     */
    void start(int port);

}
