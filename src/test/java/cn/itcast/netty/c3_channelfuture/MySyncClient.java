package cn.itcast.netty.c3_channelfuture;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author liaohongchen
 * @Description ChannelFuture的 sync 使用方式
 * @date 2022/1/15 9:21
 */
public class MySyncClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                //该方法是异步非阻塞方法，主线程调用后不会被阻塞，真正去执行连接操作的是NIO线程
                //NIO线程 ： NioEventLoop中的线程
                .connect(new InetSocketAddress("localhost", 8080));

        //该方法用于等待连接被真正建立,主线程同步阻塞等结果 ,类似jdk 的future的get()方法
        channelFuture.sync();
        //拿到客户端-服务端之间的channel对象
        Channel channel = channelFuture.channel();
        System.out.println(channel);
        // 此处打断点调试，调用 channel.writeAndFlush(...);
        channel.writeAndFlush("hello world");
        System.in.read();
    }
}
