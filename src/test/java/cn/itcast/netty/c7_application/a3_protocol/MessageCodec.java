package cn.itcast.netty.c7_application.a3_protocol;

import cn.itcast.netty.c7_application.a3_protocol.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author liaohongchen
 * @Description 自定义协议
 * @date 2022/1/25 17:22
 */
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        //设置魔术 四个字节
        out.writeBytes(new byte[]{'A','M','O','R'});
        //设置版本号 1个字节
        out.writeByte(1);
        //设置序列化方式 1个字节
        out.writeByte(1);
        //设置指令类型 1个字节
        out.writeByte(message.getMessageType());
        //设置请求序号 4个字节
        out.writeInt(message.getSequenceId());
        //为了补齐为16个字节 填充一个
        out.writeByte(0xff);

        //获得序列化后的msg
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(message);

        byte[] bytes = bos.toByteArray();

        //获得并设置正文长度 长度使用4个字节标识
        out.writeInt(bytes.length);

        out.writeBytes(bytes);

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //获得魔数
        int magic = in.readInt();
        //获取版本号
        int version = in.readByte();
        //获取序列化方式
        int seqType = in.readByte();
        //获取指令类型
        int messageType = in.readByte();
        //获得请求序号
        int sequenceId = in.readInt();
        //移除补齐字节
        in.readByte();

        //获得正文长度
        int length = in.readInt();
        //获得正文
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();

        //将信息放入list,传递给下一个handler
        out.add(message);

        System.out.println("=================魔数============");
        System.out.println(magic);
        System.out.println("=================版本号============");
        System.out.println(version);
        System.out.println("=================序列化方式============");
        System.out.println(seqType);
        System.out.println("=================指令类型============");
        System.out.println(messageType);
        System.out.println("=================请求序号============");
        System.out.println(sequenceId);
        System.out.println("=================正文长度============");
        System.out.println(length);
        System.out.println("=================正文============");
        System.out.println(message);
    }
}
