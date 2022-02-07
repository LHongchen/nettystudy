package cn.itcast.server.handler;

import cn.itcast.message.GroupQuitRequestMessage;
import cn.itcast.server.session.Session;
import cn.itcast.server.session.SessionFactory;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/27 16:21
 */
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已经断开", ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已经异常断开，异常是{}", ctx.channel(), throwable.getMessage());
    }
}
