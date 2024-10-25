package org.konoechoda.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import org.konoechoda.protocol.ProtocolConstant;

public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    /**
     * 记录解析器
     */
    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        this.recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        // 构造 parser
        RecordParser recordParser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        recordParser.setOutput(new Handler<Buffer>() {
            // 初始化
            int size = -1;
            // 一次完整的读取
            Buffer buffer = Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                if (-1 == size) {
                    // 读取消息体长度
                    size = buffer.getInt(13);
                    recordParser.fixedSizeMode(size);
                    // 写入头信息
                    this.buffer.appendBuffer(buffer);
                } else {
                    // 读取消息体
                    // 写入消息体
                    this.buffer.appendBuffer(buffer);
                    // 拼接完整 Buffer, 执行处理
                    bufferHandler.handle(this.buffer);
                    // 重置
                    recordParser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    this.buffer = Buffer.buffer();
                }
            }
        });
        return recordParser;
    }


}
