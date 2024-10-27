package org.konoechoda.fault.retry;

import org.konoechoda.fault.retry.impl.NoRetryStrategy;
import org.konoechoda.spi.SpiLoader;

/**
 * 重试策略工厂
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }
    // 默认重试策略
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * 获取默认重试策略
     * @param key 重试策略key
     * @return 默认重试策略
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}
