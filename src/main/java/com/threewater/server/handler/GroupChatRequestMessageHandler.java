package com.threewater.server.handler;

import com.threewater.message.GroupChatRequestMessage;
import com.threewater.server.session.GroupSessionFactory;
import com.threewater.server.session.Session;
import com.threewater.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/20/16:56
 * @Description: 群聊请求处理器
 */
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        List<Channel> channels = GroupSessionFactory.getGroupSession()
                .getMembersChannel(msg.getGroupName());
        for (Channel channel : channels) {
            // 不向发送者自己发送群消息
            if (channel != SessionFactory.getSession().getChannel(msg.getFrom())) {
                channel.writeAndFlush(new GroupChatRequestMessage(msg.getFrom(), msg.getGroupName(), msg.getContent()));
            }
        }
    }
}
