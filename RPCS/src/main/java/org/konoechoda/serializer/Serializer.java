package org.konoechoda.serializer;

import java.io.IOException;

public interface Serializer {

    /**
     * 序列化
     * @param obj 对象
     * @param <T> 泛型
     * @return 字节数组
     * @throws IOException IO异常
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     * 反序列化
     * @param bytes 字节数组
     * @param clazz 类
     * @param <T> 泛型
     * @return 反序列化对象
     * @throws IOException IO异常
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
