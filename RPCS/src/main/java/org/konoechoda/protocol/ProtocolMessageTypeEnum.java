package org.konoechoda.protocol;

import lombok.Getter;

/**
 * 协议消息类型枚举
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHER(3);


    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据值获取枚举
     * @param key 值
     * @return 枚举
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key) {
        for (ProtocolMessageTypeEnum type : values()) {
            if (type.key == key) {
                return type;
            }
        }
        return null;
    }
}
