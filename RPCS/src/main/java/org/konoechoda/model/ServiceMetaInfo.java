package org.konoechoda.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 注册信息
 */
@Data
public class ServiceMetaInfo {
    // 服务名称
    private String serviceName;
    // 服务版本
    private String serviceVersion = "1.0.0";
    // 服务地址
    private String serviceHost;
    // 服务端口
    private Integer servicePort;
    // 服务分组
    private String serviceGroup = "default";

    /**
     * 获取服务key
     * @return 服务key - 服务名:版本
     */
    public String getServiceKey() {
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务节点key
     * @return 服务节点key - 服务名:版本/服务地址:端口
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }

    /**
     * 获取服务地址
     * @return 服务地址 - http://服务地址:服务端口
     */
    public String getServiceAddress() {
        if(!StrUtil.contains(serviceHost, "http")) {
           return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }

}
