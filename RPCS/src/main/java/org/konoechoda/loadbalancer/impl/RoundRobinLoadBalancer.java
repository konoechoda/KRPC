package org.konoechoda.loadbalancer.impl;

import org.konoechoda.loadbalancer.LoadBalancer;
import org.konoechoda.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    /**
     * 当前索引
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

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
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        // 取模轮询
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
