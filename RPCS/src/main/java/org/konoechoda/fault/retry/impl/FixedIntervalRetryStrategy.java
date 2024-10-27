package org.konoechoda.fault.retry.impl;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.konoechoda.fault.retry.RetryStrategy;
import org.konoechoda.model.RpcResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 固定间隔重试策略
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy {

    /**
     * 重试
     *
     * @param callable 重试操作
     * @return 重试结果
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        // 构造一个重试器, 用于在指定条件下自动重试callable
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class) // 异常时重试
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS)) // 重试间隔 - 3s
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 重试次数 - 3次
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        // 记录重试日志
                        log.info("重试第{}次", attempt.getAttemptNumber());
                    }
                })// 重试监听器
                .build();// 构建重试器
        return retryer.call(callable);
    }
}
