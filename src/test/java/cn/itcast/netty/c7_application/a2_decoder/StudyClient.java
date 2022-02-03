package cn.itcast.netty.c7_application.a2_decoder;

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

import java.nio.charset.StandardCharsets;
import java.util.Random;


/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/19 21:38
 */
@Slf4j
public class StudyClient {

    public static void main(String[] args) {


        send();

    }


    static void send(){

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

                                    final  int maxLength = 64;

                                    char c = 'a';

                                    for (int i = 0; i < 10; i++) {
                                        //每次发送16个字节，发送10次
                                        ByteBuf buffer = ctx.alloc().buffer(maxLength);

                                        Random random = new Random();
                                        StringBuilder sb = new StringBuilder();

                                        for (int j = 0; j < (int)(random.nextInt(maxLength-2)); j++) {
                                            sb.append(c);
                                        }

                                        sb.append("\\c");

                                        buffer.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8));
                                        c++;
                                        ctx.writeAndFlush(buffer);
                                        super.channelActive(ctx);
                                    }


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
