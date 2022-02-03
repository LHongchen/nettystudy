package cn.itcast.netty.c3_channelfuture;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author liaohongchen
 * @Description ChannelFuture的addListener使用方式
 * @date 2022/1/15 9:21
 */
public class MyAddListenerClient {
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

        //加一个监听器 这样建立连接之后 NIO线程会去走operationComplete方法，而不是mian线程，相当于异步了
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                channel.writeAndFlush("34534");
            }
        });
        System.in.read();
    }
}
