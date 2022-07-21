package com.threewater.server.handler;

import com.threewater.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/21/10:59
 * @Description: 退出请求处理器
 */
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {

    // 连接断开时会触发 inactive 事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String username = SessionFactory.getSession().getUsername(ctx.channel());
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已经断开", username);
    }

    // 当捕捉到异常时会触发
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String username = SessionFactory.getSession().getUsername(ctx.channel());
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已经异常断开 异常是 {}", username, cause.getMessage());
    }
}
