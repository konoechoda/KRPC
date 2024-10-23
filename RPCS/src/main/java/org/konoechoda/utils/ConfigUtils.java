package org.konoechoda.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 工具类配置
 */
public class ConfigUtils {

    /**
     * 加载配置对象
     * @param clazz 配置类
     * @param prefix 配置前缀
     * @return 配置对象
     * @param <T> 配置对象类型
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix) {
        return loadConfig(clazz, prefix, "");
    }

    /**
     * 加载配置对象 - 支持分区环境
     * @param clazz 配置类
     * @param prefix 配置前缀
     * @param env 环境
     * @return 配置对象
     * @param <T> 配置对象类型
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix, String env) {
        // 构建配置文件名称
        StringBuilder configFileName = new StringBuilder("application");
        if (StrUtil.isNotBlank(env)) {
            configFileName.append("-").append(env);
        }
        configFileName.append(".properties");
        // 根据配置文件名称初始化配置对象
        Props props = new Props(configFileName.toString());
        return props.toBean(clazz, prefix);
    }

}
