package org.konoechoda.protocol;

import lombok.Getter;

/**
 * 协议消息状态枚举
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok", 200),
    BAD_REQUEST("bad request", 400),
    BAD_RESPONSE("bad response", 500);

    private final String text;
    private final int code;

    ProtocolMessageStatusEnum(String text, int code) {
        this.text = text;
        this.code = code;
    }

    /**
     * 根据状态码获取枚举
     * @param code 状态码
     * @return 枚举
     */
    public static ProtocolMessageStatusEnum getByCode(int code) {
        for (ProtocolMessageStatusEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
