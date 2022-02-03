package cn.itcast.netty.c7_application.a3_protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/25 16:52
 */
@Slf4j
public class HttpProtocolTest {
    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG))
                                .addLast(new HttpServerCodec())
                                .addLast(new SimpleChannelInboundHandler<HttpRequest>() {

                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws Exception {
                                        log.debug(httpRequest.uri());

                                        DefaultFullHttpResponse response = new DefaultFullHttpResponse(httpRequest.protocolVersion(),
                                                HttpResponseStatus.OK);

                                        byte[] bytes = "<h1>hello,netty</h1>".getBytes(StandardCharsets.UTF_8);

                                        response.headers().setInt(CONTENT_LENGTH, bytes.length);

                                        response.content().writeBytes(bytes);

                                        ctx.writeAndFlush(response);

                                    }
                                });
                    }
                }).bind(8080);

    }
}
