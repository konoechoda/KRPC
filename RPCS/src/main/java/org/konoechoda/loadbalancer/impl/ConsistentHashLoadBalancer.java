package org.konoechoda.loadbalancer.impl;

import org.konoechoda.loadbalancer.LoadBalancer;
import org.konoechoda.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 虚拟节点 - 一致性哈希环
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_SIZE = 100;

    /**
     * 服务选择器
     *
     * @param params              请求参数
     * @param serviceMetaInfoList 服务列表
     * @return 选择的服务
     */
    @Override
    public ServiceMetaInfo select(Map<String, Object> params, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // 构建一致性哈希环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                int hash = getHash(serviceMetaInfo.getServiceKey() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }
        // 取请求参数的哈希值
        int hash = getHash(params);
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * 获取哈希值
     *
     * @param key 键
     * @return 哈希值
     */
    private int getHash(Object key) {
        return key.hashCode();
    }

}
