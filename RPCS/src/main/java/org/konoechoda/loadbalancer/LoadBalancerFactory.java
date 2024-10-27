package org.konoechoda.loadbalancer;

import org.konoechoda.loadbalancer.impl.RoundRobinLoadBalancer;
import org.konoechoda.spi.SpiLoader;

/**
 * 负载均衡器工厂
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }
    // 默认负载均衡器
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();
    /**
     * 获取默认负载均衡器
     * @param key 负载均衡器key
     * @return 默认负载均衡器
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
