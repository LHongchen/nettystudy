package cn.itcast.netty.c6_bytebuf;

import io.netty.buffer.ByteBuf;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/19 19:42
 */
public class ByteBufUtil {

    public  static void log(ByteBuf byteBuf){
        int length = byteBuf.readableBytes();
        int row = length / 16 + (length % 15 == 0 ? 0 : 1)+4;
        StringBuilder buf = new StringBuilder(row * 80 * 2)
                .append("read index:").append(byteBuf.readerIndex())
                .append(" write index:").append(byteBuf.writerIndex())
                .append(" capacity:").append(byteBuf.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, byteBuf);
        System.out.println(buf.toString());

    }
}
