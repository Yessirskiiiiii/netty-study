package com.threewater.netty.eventLoop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/11/15:03
 * @Description: 处理 io 事件
 */
@Slf4j
public class EvenLoopServer {
    public static void main(String[] args) {
        // 细分2：创建一个独立的 EventLoopGroup
        EventLoopGroup group = new DefaultEventLoop();
        new ServerBootstrap()
                // 细分1：boss 只负责 ServerSocketChannel 上 accept 事件；worker 只负责 socketChannel 上的读写
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                            @Override                                          // ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.debug(byteBuf.toString(Charset.defaultCharset()));
                                ctx.fireChannelRead(msg); // 将消息传递给下一个 handler
                            }
                        }).addLast(group, "handler2", new ChannelInboundHandlerAdapter() {
                            @Override                                          // ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.debug(byteBuf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
