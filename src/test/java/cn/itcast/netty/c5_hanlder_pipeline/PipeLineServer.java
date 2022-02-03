package cn.itcast.netty.c5_hanlder_pipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @author liaohongchen
 * @Description 重点关注出站入站 ，在socketChannel.write会从tail从后往前找outbound
 *                          在ctx.write会从当前handler往前找
 * @date 2022/1/19 11:58
 */
public class PipeLineServer {
    public static void main(String[] args) {


        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 在socketChannel的pipeline中添加handler
                        // pipeline中handler是带有head与tail节点的双向链表，的实际结构为
                        // head <-> handler1 <-> ... <-> handler4 <->tail
                        // Inbound主要处理入站操作，一般为读操作，发生入站操作时会触发Inbound方法
                        // 入站时，handler是从head向后调用的
                        socketChannel.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(Thread.currentThread().getName() + ":Inbound handler 1");
                                // 父类该方法内部会调用fireChannelRead
                                // 将数据传递给下一个handler
                                super.channelRead(ctx, msg);
                            }
                        });
                        socketChannel.pipeline().addLast("handler2", new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(Thread.currentThread().getName() + ":Inbound handler 2");

                                //重点 ！！当handler中调用该方法进行写操作时，会触发Outbound操作，此时是从 tail向前 寻找OutboundHandler
//                                socketChannel.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes(StandardCharsets.UTF_8)));
                                //重点 ！！当handler中调用该方法进行写操作时，会触发Outbound操作，此时是从 当前handler 寻找OutboundHandler
                                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes(StandardCharsets.UTF_8)));

                                super.channelRead(ctx, msg);
                            }
                        });
                        // Outbound主要处理出站操作，一般为写操作，发生出站操作时会触发Outbound方法
                        // 出站时，handler的调用是从tail向前调用的
                        socketChannel.pipeline().addLast("handler3", new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println(Thread.currentThread().getName() + ":Outbound handler 3");
                                super.write(ctx, msg, promise);
                            }
                        });

                        socketChannel.pipeline().addLast("handler4", new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println(Thread.currentThread().getName() + ":Outbound handler 4");
                                super.write(ctx, msg, promise);
                            }
                        });

                    }
                }).bind(8080);

    }
}
