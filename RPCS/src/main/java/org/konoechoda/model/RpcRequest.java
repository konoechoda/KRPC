package org.konoechoda.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // 参数类型
    private Class<?>[] paramsTypes;

    // 参数
    private Object[] params;
}
