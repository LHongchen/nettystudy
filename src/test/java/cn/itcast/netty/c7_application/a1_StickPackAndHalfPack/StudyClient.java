package cn.itcast.netty.c7_application.a1_StickPackAndHalfPack;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;


/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/19 21:38
 */
@Slf4j
public class StudyClient {

    public static void main(String[] args) {

        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(worker)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            log.debug("connected...");
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    log.debug("send...");
                                    //每次发送16个字节，发送10次
                                    for (int i = 0; i < 10; i++) {
                                        ByteBuf buffer = ctx.alloc().buffer();
                                        buffer.writeBytes(new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
                                        ctx.writeAndFlush(buffer);
                                    }
                                    super.channelActive(ctx);
                                }
                            });
                        }
                    });

            ChannelFuture future = bootstrap.connect("localhost", 8080);

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }


    }


}
