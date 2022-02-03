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
import java.util.Scanner;

/**
 * @author liaohongchen
 * @Description 处理关闭channel 如何优雅关闭
 * @date 2022/1/18 21:47
 */
public class MyCloseChannelClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        //创建NioEventLoopGroup 使用完之后关闭
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
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

        Scanner scanner = new Scanner(System.in);

        //创建一个线程用于输入并将输入数据发送到服务器
        new Thread(()->{
            while(true){
                String msg = scanner.next();
                if("q".equals(msg)){
                    //关闭操作是异步的，会在NIO线程中关闭，类似connect方法
                    channel.close();
                    break;
                }
                channel.writeAndFlush(msg);
            }
        }, "inputThread").start();


        //获得closeFuture对象
        ChannelFuture closeFuture = channel.closeFuture();
        System.out.println("waiting close...");

        //1、同步等待NIO线程执行完close操作 2、增加监听器
        closeFuture.sync();

        //关闭以后执行一些操作，可以保证执行的操作一定是在channel关闭之后执行的
        System.out.println("关闭channel后执行一些额外的操作。。。");

        //优雅的关闭EventLoopGroup
        group.shutdownGracefully();

    }

}
