package com.threewater.client;

import com.threewater.client.handler.RpcResponseMessageHandler;
import com.threewater.protocol.MessageCodecSharable;
import com.threewater.protocol.ProtocolFrameDecoder;
import com.threewater.server.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/28/10:55
 * @Description: 用于创建客户端向服务器端发送的 channel
 */
@Slf4j
public class RpcClientManager {

    public static void main(String[] args) {
        HelloService service = ProxyFactory.getProxyService(HelloService.class);
        System.out.println(service.sayHello("张三"));
    }

    private volatile static Channel channel = null;
    private static final Object LOCK = new Object();

    // 初始化方法只执行一次，因此使用单例模式
    public static Channel getChannel() {
        if (channel == null) {
            synchronized (LOCK) {
                if (channel == null) {
                    initChannel();
                }
            }
        }
        return channel;
    }

    // 初始化 channel
    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocolFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODEC);
                ch.pipeline().addLast(RPC_HANDLER);
            }
        });
        try {
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            // 改成异步操作，避免阻塞
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            log.error("client error", e);
        }
    }

}
