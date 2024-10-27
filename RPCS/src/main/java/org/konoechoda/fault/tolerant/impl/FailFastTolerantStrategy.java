package org.konoechoda.fault.tolerant.impl;

import org.konoechoda.fault.tolerant.TolerantStrategy;
import org.konoechoda.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败容错策略
 */
public class FailFastTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("Service error: ", e);
    }
}
