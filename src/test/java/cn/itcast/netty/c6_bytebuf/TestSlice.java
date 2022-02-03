package cn.itcast.netty.c6_bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/19 20:32
 */
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16, 20);

        buffer.writeBytes(new byte[]{1,2,3,4,5,6,7,8,9,10});

        //把buf切片 逻辑意义上
        ByteBuf slice1 = buffer.slice(0, 5);
        ByteBuf slice2 = buffer.slice(5, 5);

        //需要让分片的buf 引用计数 + 1
        //避免原buf释放 导致分片buf不可用
        slice1.retain();
        slice2.retain();

        ByteBufUtil.log(slice1);
        ByteBufUtil.log(slice2);

        //修改原数据
        buffer.setByte(0,7);
        ByteBufUtil.log(buffer);
        ByteBufUtil.log(slice1);

    }
}
