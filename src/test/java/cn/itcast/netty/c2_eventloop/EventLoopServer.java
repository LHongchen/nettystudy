package cn.itcast.netty.c2_eventloop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/11 21:43
 */
public class EventLoopServer {
    public static void main(String[] args) {

        //自定义EventLoopGroup 可处理普通任务与定时任务 不能处理io任务
        EventLoopGroup mygroup = new DefaultEventLoopGroup();

        new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println(Thread.currentThread().getName()+":"+buf.toString(StandardCharsets.UTF_8));
                                ctx.fireChannelRead(msg);//出发下一个handler
                            }
                        })
                                .addLast(mygroup,"myhandler",new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println(Thread.currentThread().getName()+":"+buf.toString(StandardCharsets.UTF_8));
                                    }
                                });
                    }
                })
                .bind(8080);
    }
}
