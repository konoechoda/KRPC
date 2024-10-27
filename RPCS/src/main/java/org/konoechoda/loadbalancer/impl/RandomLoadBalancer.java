package org.konoechoda.loadbalancer.impl;

import org.konoechoda.loadbalancer.LoadBalancer;
import org.konoechoda.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机负载均衡器
 */
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    /**
     * 服务选择器
     *
     * @param params              请求参数
     * @param serviceMetaInfoList 服务列表
     * @return 选择的服务
     */
    @Override
    public ServiceMetaInfo select(Map<String, Object> params, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        // 随机选择
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
