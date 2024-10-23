package org.konoechoda.config;

import lombok.Data;
import org.konoechoda.serializer.SerializerKey;

/**
 * RPC配置
 */
@Data
public class RpcConfig {
    // 名称
    private String name = "KRPC";
    // 版本号
    private String version = "1.0.0";
    // 服务器主机名
    private String host = "localhost";
    // 服务器端口
    private Integer port = 10010;
    // 模拟调用
    private Boolean mock = false;
    // 序列化器
    private String serializer = SerializerKey.JDK;
    // 注册中心配置
    private RegisterConfig register = new RegisterConfig();
}
