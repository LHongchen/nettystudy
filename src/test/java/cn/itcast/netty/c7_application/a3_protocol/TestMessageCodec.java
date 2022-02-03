package cn.itcast.netty.c7_application.a3_protocol;

import cn.itcast.netty.c7_application.a3_protocol.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import sun.plugin2.message.Message;
import sun.plugin2.message.Serializer;

import java.io.IOException;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/25 17:46
 */
public class TestMessageCodec {
    public static void main(String[] args) throws Exception {

        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,12,4,0,0),
                new LoggingHandler(LogLevel.DEBUG),
                new MessageCodec()
        );

        LoginRequestMessage msg = new LoginRequestMessage("amor", "123");

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, msg, buffer);

        channel.writeInbound(buffer);

    }
}
