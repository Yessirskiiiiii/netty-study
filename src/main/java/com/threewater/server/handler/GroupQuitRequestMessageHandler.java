package com.threewater.server.handler;

import com.threewater.message.GroupQuitRequestMessage;
import com.threewater.message.GroupQuitResponseMessage;
import com.threewater.server.session.Group;
import com.threewater.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/20/17:00
 * @Description: 退出群聊请求处理器
 */
@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().removeMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupQuitResponseMessage(true, "已退出群" + msg.getGroupName()));
        } else {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false, msg.getGroupName() + "群不存在"));
        }
    }
}
