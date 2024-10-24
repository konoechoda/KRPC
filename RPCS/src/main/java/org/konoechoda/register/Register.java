package org.konoechoda.register;

import org.konoechoda.config.RegisterConfig;
import org.konoechoda.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心
 */
public interface Register {
    /**
     * 初始化
     * @param registerConfig 配置
     */
    void init(RegisterConfig registerConfig);

    /**
     * 注册服务 - 服务端
     * @param serviceMetaInfo 注册信息
     * @throws Exception 异常
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务 - 服务端
     * @param serviceMetaInfo 服务信息
     * @throws Exception 异常
     */
    void unregister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务发现
     * @param serviceKey key
     * @return 服务信息
     * @throws Exception 异常
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws Exception;

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测
     */
    void heartbeat();

    /**
     * 监听
     * @param serviceKey key
     */
    void watch(String serviceKey);

}
