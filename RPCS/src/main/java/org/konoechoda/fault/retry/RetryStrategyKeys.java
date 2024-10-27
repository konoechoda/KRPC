package org.konoechoda.fault.retry;

/**
 * 重试策略常量
 */
public interface RetryStrategyKeys {

    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定间隔重试策略
     */
    String FIXED_INTERVAL = "fixedInterval";


}
