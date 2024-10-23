package org.konoechoda.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.konoechoda.constant.RpcConstant;

import java.io.Serializable;

/**
 * RPC请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    // 服务接口名
    private String interfaceName;

    // 服务方法名
    private String methodName;

    // 服务版本
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;

    // 参数类型
    private Class<?>[] paramsTypes;

    // 参数
    private Object[] params;
}
