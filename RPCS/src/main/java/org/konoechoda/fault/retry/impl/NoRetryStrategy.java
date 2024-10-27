package org.konoechoda.fault.retry.impl;

import org.konoechoda.fault.retry.RetryStrategy;
import org.konoechoda.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试策略
 */
public class NoRetryStrategy implements RetryStrategy {
    /**
     * 重试
     *
     * @param callable 重试操作
     * @return 重试结果
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
