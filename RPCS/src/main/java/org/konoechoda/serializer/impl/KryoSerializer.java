package org.konoechoda.serializer.impl;
 
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.konoechoda.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
 
/**
 * Kryo序列化方式
 */
public class KryoSerializer implements Serializer {
    /**
     *Kryo线程不安全，使用ThreadLocal保证每一个线程只有一个Kryo实例
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
//设置动态序列化和反序列化类，不提前注册所有类(可能存在安全问题)
        kryo.setRegistrationRequired(false);
        return kryo;
    });
 
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output outPut = new Output(byteArrayOutputStream);
        KRYO_THREAD_LOCAL.get().writeObject(outPut, object);
        outPut.close();
        return byteArrayOutputStream.toByteArray();
 
    }
 
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Input input = new Input(byteArrayInputStream);
        T result = KRYO_THREAD_LOCAL.get().readObject(input, clazz);
        input.close();
        return result;
    }
}