package org.konoechoda.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 协议消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolMessage<T> {

    // 消息头
    private Header header;

    // 消息体
    private T body;

    //消息协议头
    @Data
    public static class Header {
        // 魔数
        private byte magic;
        // 协议版本
        private byte version;
        // 序列化器
        private byte serializer;
        // 消息类型(请求/响应)
        private byte type;
        // 状态
        private byte status;
        // 消息 ID
        private long requestId;
        // 数据长度
        private int length;
    }

}
