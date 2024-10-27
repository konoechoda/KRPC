package org.konoechoda.fault.tolerant.impl;

import lombok.extern.slf4j.Slf4j;
import org.konoechoda.fault.tolerant.TolerantStrategy;
import org.konoechoda.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略 - 静默处理异常
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.error("Service error: ", e);
        return new RpcResponse();
    }
}
