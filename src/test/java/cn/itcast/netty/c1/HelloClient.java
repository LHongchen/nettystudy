package cn.itcast.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/10 16:48
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //1、客户端启动器，负责装配netty组件，启动器
        new Bootstrap()
                //2、创建 NioEventLoopGroup 后者可以理解为 线程池 + selector
                .group(new NioEventLoopGroup())
                //3、选择服务器的 SocketChannel 实现
                .channel(NioSocketChannel.class)
                //4、ChannelInitializer只执行一次
                //建立连接后，initChannel 便于后面添加更多的handler
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        //5、Channel的处理器 StringEncoder 用于编码 String => ByteBuf
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                //6、指定要连接的服务器端口
                .connect("localhost",8080)
                //7、netty中有很多方法都是异步非阻塞的，如connect
                //这时候需要调用sync()方法等待连接建立，阻塞等待
                .sync()
                //8、获取channel通道对象，为了实现数据读写
                .channel()
                //9、写入数据
                .writeAndFlush("hello,world");
    }
}
