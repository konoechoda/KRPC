package org.konoechoda.fault.tolerant.impl;

import org.konoechoda.fault.tolerant.TolerantStrategy;
import org.konoechoda.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略 - 故障转移
 */
public class FailOverTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // TODO 故障转移
        return null;
    }
}
