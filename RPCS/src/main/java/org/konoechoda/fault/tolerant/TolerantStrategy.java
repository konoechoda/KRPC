package org.konoechoda.fault.tolerant;

import org.konoechoda.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 */
public interface TolerantStrategy {

    RpcResponse doTolerant(Map<String, Object> context, Exception e);

}
