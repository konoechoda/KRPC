package org.konoechoda.loadbalancer;

import org.konoechoda.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器
 */
public interface LoadBalancer {

    /**
     * 服务选择器
     *
     * @param params 请求参数
     * @param serviceMetaInfoList 服务列表
     * @return 选择的服务
     */
    ServiceMetaInfo select(Map<String, Object> params, List<ServiceMetaInfo> serviceMetaInfoList);
}
