package cn.itcast.netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/10 16:30
 */
public class HelloServer {

    public static void main(String[] args) {
        //1、服务端启动器，负责装配netty组件，启动器
        new ServerBootstrap()
                //2、创建 NioEventLoopGroup 后者可以理解为 单线程的线程池 + selector
                .group(new NioEventLoopGroup())
                //3、选择服务器的 ServerSocketChannel 实现，除此之外还有三种实现 OIO EPOLL MAC的
                .channel(NioServerSocketChannel.class)
                //4、childHandler 负责读写 决定了handler执行哪些操作 责任链模式
                // ChannelInitializer 建立连接后只执行一次
                //执行initChannel 以便添加更多handler
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        //5、SocketChannel的处理器 StringDecoder 用于解码 ByteBuf => String
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        //6、SocketChannel的自定义业务处理器 使用上一个处理器的结果
                        nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                System.out.println(s);
                            }
                        });
                    }
                })
                //ServerSocketChannel绑定8080端口
                .bind(8080);
    }
}
