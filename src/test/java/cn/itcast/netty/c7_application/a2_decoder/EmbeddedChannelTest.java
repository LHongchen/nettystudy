package cn.itcast.netty.c7_application.a2_decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/25 16:30
 */
public class EmbeddedChannelTest {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,1, 4, 1, 0),
                new LoggingHandler(LogLevel.DEBUG)
        );

        //模拟客户端写入数据
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "hello");
        channel.writeInbound(buffer);


    }

    private static void send(ByteBuf byteBuf, String msg){
        int length = msg.length();
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);

        byteBuf.writeByte(0xCA);

        byteBuf.writeInt(length);

        byteBuf.writeByte(0xFE);

        byteBuf.writeBytes(bytes);
    }

}
