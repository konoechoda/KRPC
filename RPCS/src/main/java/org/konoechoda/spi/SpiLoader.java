package org.konoechoda.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.konoechoda.serializer.Serializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {

    /**
     * 存储已经加载的类 key:接口名称 value:(key:实现类名称 value:实现类)
     */
    private static final Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象存储缓存
     */
    private static final Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统SPI目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户SPI目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_PATH = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = List.of(Serializer.class);

    /**
     * 加载所有的类
     */
    public static void loadAll() {
        log.info("load all spi class");
        for(Class<?> clazz : LOAD_CLASS_LIST) {
            load(clazz);
        }
    }

    public static <T> T getInstance(Class<T> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader: %s not found", tClassName));
        }
        if(!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader: %s not found key=%S", tClassName, key));
        }
        // 获取到要加载的实例
        Class<?> implClass = keyClassMap.get(key);
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(String.format("SpiLoader: %s newInstance error", implClassName), e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }

    public static Map<String, Class<?>> load(Class<?> clazz) {
        log.info("load spi class: {}", clazz.getName());
        // 扫描路径
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_PATH) {
            List<URL> resources = ResourceUtil.getResources(scanDir + clazz.getName());
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArr = line.split("=");
                        if (strArr.length > 1) {
                            String key = strArr[0];
                            String className = strArr[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("load spi class error", e);
                }
            }
        }
        loaderMap.put(clazz.getName(), keyClassMap);
        return keyClassMap;
    }

}
