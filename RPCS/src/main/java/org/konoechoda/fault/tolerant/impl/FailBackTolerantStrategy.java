package org.konoechoda.fault.tolerant.impl;

import org.konoechoda.fault.tolerant.TolerantStrategy;
import org.konoechoda.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略 - 降级到其他服务
 */
public class FailBackTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // TODO 降级到其他服务
        return null;
    }
}
