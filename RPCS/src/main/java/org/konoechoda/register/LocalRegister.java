package org.konoechoda.register;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地注册
 */
public class LocalRegister {

    // 存储注册信息
    private static Map<String, Class<?>> map = new HashMap<>();

    /**
     * 获取实现类
     * @param interfaceName 接口名
     * @param implClass 实现类
     */
    public static void register(String interfaceName, Class<?> implClass) {
        map.put(interfaceName, implClass);
    }

    /**
     * 获取实现类
     * @param interfaceName 接口名
     * @return 实现类
     */
    public static Class<?> get(String interfaceName) {
        return map.get(interfaceName);
    }

    /**
     * 移除实现类
     * @param interfaceName 接口名
     */
    public static void remove(String interfaceName) {
        map.remove(interfaceName);
    }


}
