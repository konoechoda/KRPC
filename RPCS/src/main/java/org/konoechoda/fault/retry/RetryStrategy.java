package org.konoechoda.fault.retry;

import org.konoechoda.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 */
public interface RetryStrategy {

    /**
     * 重试
     *
     * @param callable 重试操作
     * @return 重试结果
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;

}
