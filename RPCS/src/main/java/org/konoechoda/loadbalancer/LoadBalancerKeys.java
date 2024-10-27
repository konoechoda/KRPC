package org.konoechoda.loadbalancer;

/**
 * 负载均衡器常量
 */
public interface LoadBalancerKeys {

    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 一致性哈希
     */
    String CONSISTENT_HASH = "consistentHash";

    /**
     * 随机
     */
    String RANDOM = "random";
}
