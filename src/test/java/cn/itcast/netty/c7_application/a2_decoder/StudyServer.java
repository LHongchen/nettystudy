package cn.itcast.netty.c7_application.a2_decoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/19 20:43
 */
@Slf4j
public class StudyServer {

    /**
     * 粘包现象:
     * 可见虽然客户端是分别以16字节为单位，通过channel向服务器发送了10次数据，
     * 可是服务器端却只接收了一次，接收数据的大小为160B，即客户端发送的数据总大小，这就是粘包现象
     *
     * 半包现象：
     *可见客户端每次发送的数据，因channel容量不足，无法将发送的数据一次性接收，便产生了半包现象
     *
     * 发生粘包与半包现象的本质是因为 TCP 是流式协议，消息无边界
     *
     */

    void start(){
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(boss, worker)
                    //加上此参数 半包现象
//                    .option(ChannelOption.SO_RCVBUF, 10)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf bufSet = ch.alloc().buffer().writeBytes("\\c".getBytes(StandardCharsets.UTF_8));
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(64, ch.alloc().buffer().writeBytes(bufSet)));
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    //连接建立时候会执行该方法
                                    log.debug("connected {}", ctx.channel());
                                    super.channelActive(ctx);
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    //连接断开的时候会执行这个方法
                                    log.debug("disconnect {}", ctx.channel());
                                    super.channelInactive(ctx);
                                }
                            });
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(8080);
            log.debug("{} binding...", future.channel());

            future.sync();

            log.debug("{} bound...", future.channel());
            //关闭channel
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            log.debug("stopped");
        }

    }

    public static void main(String[] args) {
        new StudyServer().start();
    }
}
