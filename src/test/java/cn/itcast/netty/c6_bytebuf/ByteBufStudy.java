package cn.itcast.netty.c6_bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/19 20:11
 */
public class ByteBufStudy {

    public static void main(String[] args) {
        //创建ByteBuf
//        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(16);
//        ByteBufUtil.log(byteBuf);
//
//        //向buf中写入数据
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 20; i++) {
//            sb.append("a");
//        }
//        byteBuf.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8));
//        //查看写入结果
//        ByteBufUtil.log(byteBuf);

        //三种不同的内存 直接内存 堆内存 池化直接内存
//        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(16);
//        System.out.println(byteBuf.getClass());
//
//        ByteBuf byteBuf1 = ByteBufAllocator.DEFAULT.heapBuffer(16);
//        System.out.println(byteBuf1.getClass());
//
//        ByteBuf byteBuf2 = ByteBufAllocator.DEFAULT.directBuffer();
//        System.out.println(byteBuf2.getClass());

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16, 20);
        ByteBufUtil.log(buffer);

        buffer.writeBytes(new byte[]{1,2,3,4});
        ByteBufUtil.log(buffer);

        buffer.writeInt(5);
        ByteBufUtil.log(buffer);

        buffer.writeInt(6);
        ByteBufUtil.log(buffer);

        buffer.writeInt(7);
        ByteBufUtil.log(buffer);

    }
}
