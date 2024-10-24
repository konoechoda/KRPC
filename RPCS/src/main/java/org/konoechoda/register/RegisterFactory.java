package org.konoechoda.register;

import org.konoechoda.register.impl.EtcdRegistry;
import org.konoechoda.spi.SpiLoader;

/**
 * 注册中心工厂
 */
public class RegisterFactory {
    static {
        SpiLoader.load(Register.class);
    }

    /**
     * 默认注册中心 - etcd
     */
    private static final Register DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取注册中心实例
     * @param key key
     * @return 实例
     */
    public static Register getInstance(String key) {
        return SpiLoader.getInstance(Register.class, key);
    }

}
