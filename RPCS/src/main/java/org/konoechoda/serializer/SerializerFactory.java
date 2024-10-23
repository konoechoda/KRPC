package org.konoechoda.serializer;

import org.konoechoda.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

    // 序列化器Map
//    private static Map<String, Serializer> SERIALIZER_MAP = new HashMap<>(){
//        {
//            put(SerializerKey.JDK, new org.konoechoda.serializer.impl.JdkSerializer());
//            put(SerializerKey.HESSIAN, new org.konoechoda.serializer.impl.HessianSerializer());
//            put(SerializerKey.JSON, new org.konoechoda.serializer.impl.JsonSerializer());
//            put(SerializerKey.KRYO, new org.konoechoda.serializer.impl.KryoSerializer());
//        }
//    };

    static {
        SpiLoader.load(Serializer.class);
    }

    // 默认序列化器
    private static final Serializer DEFAULT_SERIALIZER = SpiLoader.getInstance(Serializer.class, SerializerKey.JDK);

    /**
     * 获取序列化器
     *
     * @param key 序列化器Key
     * @return 序列化器
     */
    public static Serializer getSerializer(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
