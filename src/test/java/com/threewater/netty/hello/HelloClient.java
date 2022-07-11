package com.threewater.netty.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/08/16:32
 * @Description: netty 入门 - 客户端
 */
public class HelloClient {

    public static void main(String[] args) throws InterruptedException {
        // 1. 启动类
        new Bootstrap()
                // 2. 添加 EventLoop
                .group(new NioEventLoopGroup())
                // 3. 选择客户端 channel 实现
                .channel(NioSocketChannel.class)
                // 4. 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 在建立连接后被调用
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                // 5. 连接到服务器
                // 异步非阻塞，main 发起了调用，真正执行 connect 是 nio 线程
                // 带有 Future，Promise 的类型都是和异步方法配套使用，用来处理结果
                .connect(new InetSocketAddress("localhost", 8080))
                // 使用 sync 方法同步处理结果
                .sync() // 阻塞住当前线程，直到 nio 线程连接建立完毕
                .channel()
                // 6. 向服务器发送数据
                .writeAndFlush("hello");
    }

}
