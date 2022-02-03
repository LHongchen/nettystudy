package cn.itcast.netty.c2_eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/11 21:51
 */
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException, IOException {

        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8080)
                .sync()
                .channel();
        System.out.println(channel);
//        channel.writeAndFlush("123");
        //这有个坑 debug模式默认回把所有线程都停下来
        System.out.println("");

    }
}
