package cn.itcast.server.handler;

import cn.itcast.message.GroupJoinRequestMessage;
import cn.itcast.message.GroupMembersRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/27 16:21
 */
@ChannelHandler.Sharable
public class GroupMembersRequestMessageHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {

    }

}