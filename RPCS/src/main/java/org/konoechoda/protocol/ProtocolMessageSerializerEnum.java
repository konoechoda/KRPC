package org.konoechoda.protocol;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议消息序列化器枚举
 */
@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(0, "jdk"),
    JSON(1, "json"),
    KRYO(2, "kryo"),
    HESSIAN(3, "hessian");

    private final int key;
    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取值列表
     * @return 值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 key 获取枚举
     * @param key key
     * @return 枚举
     */
    public static ProtocolMessageSerializerEnum getByKey(int key) {
        for (ProtocolMessageSerializerEnum value : values()) {
            if (value.key == key) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据 value 获取枚举
     * @param value value
     * @return 枚举
     */
    public static ProtocolMessageSerializerEnum getByValue(String value) {
        for (ProtocolMessageSerializerEnum type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }

}
