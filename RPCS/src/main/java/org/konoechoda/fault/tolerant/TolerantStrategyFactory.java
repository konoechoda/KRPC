package org.konoechoda.fault.tolerant;

import org.konoechoda.fault.tolerant.impl.FailFastTolerantStrategy;
import org.konoechoda.spi.SpiLoader;

/**
 * 容错策略工厂
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    // 默认容错策略
    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FailFastTolerantStrategy();

    /**
     * 获取容错策略
     * @param key 容错策略key
     * @return 容错策略
     */
    public static TolerantStrategy getInstance(String key){
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
