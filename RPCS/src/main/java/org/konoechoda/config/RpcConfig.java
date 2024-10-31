package org.konoechoda.config;

import lombok.Data;
import org.konoechoda.fault.retry.RetryStrategyKeys;
import org.konoechoda.fault.tolerant.TolerantStrategyKeys;
import org.konoechoda.loadbalancer.LoadBalancerKeys;
import org.konoechoda.serializer.SerializerKey;
import org.konoechoda.server.ServiceProtocolKeys;

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
    // proxy
    private String protocol = ServiceProtocolKeys.TCP;
    // 模拟调用
    private Boolean mock = false;
    // 序列化器
    private String serializer = SerializerKey.JDK;
    // 注册中心配置
    private RegisterConfig registerConfig = new RegisterConfig();
    // 负载均衡器
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
    // 重试策略
    private String retryStrategy = RetryStrategyKeys.NO;
    // 容错策略
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
