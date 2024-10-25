import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;
import org.konoechoda.constant.RpcConstant;
import org.konoechoda.model.RpcRequest;
import org.konoechoda.protocol.*;

import java.io.IOException;

public class ProtocolMessageTest {

    @Test
    public void testEncodeAndDecode() throws IOException {
        // 构造消息
        ProtocolMessage<RpcRequest> message = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getCode());
        header.setRequestId(0);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName("org.konoechoda.service.HelloService");
        rpcRequest.setMethodName("sayHello");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParamsTypes(new Class<?>[]{String.class});
        rpcRequest.setParams(new Object[]{"hello", "world"});
        message.setHeader(header);
        message.setBody(rpcRequest);

        Buffer encode = ProtocolMessageEncoder.encode(message);
        ProtocolMessage<?> decode = ProtocolMessageDecoder.decode(encode);
        Assert.assertNotNull(decode);

    }
}
