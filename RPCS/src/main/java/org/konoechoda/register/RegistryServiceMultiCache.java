package org.konoechoda.register;

import org.konoechoda.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServiceMultiCache {
    /**
     * 服务缓存
     */
    Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * 写缓存
     *
     * @param serviceKey 服务键名
     * @param newServiceCache 更新后的缓存列表
     * @return
     */
    public void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache.put(serviceKey, newServiceCache);
    }

    /**
     * 读缓存
     *
     * @param serviceKey
     * @return
     */
    public List<ServiceMetaInfo> readCache(String serviceKey) {
        return this.serviceCache.get(serviceKey);
    }

    /**
     * 清空缓存
     */
    public void clearCache(String serviceKey) {
        this.serviceCache.remove(serviceKey);
    }
}
