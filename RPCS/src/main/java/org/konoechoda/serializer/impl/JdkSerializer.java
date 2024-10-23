package org.konoechoda.serializer.impl;

import org.konoechoda.serializer.Serializer;

import java.io.*;

public class JdkSerializer implements Serializer {
    /**
     * 序列化
     *
     * @param object 对象
     * @return 字节数组
     * @throws IOException IO异常
     */
    @Override

    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 反序列化
     *
     * @param bytes 字节数组
     * @param type 类
     * @return 反序列化对象
     * @throws IOException IO异常
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            objectInputStream.close();
        }
    }
}
