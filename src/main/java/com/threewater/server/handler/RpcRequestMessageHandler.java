package com.threewater.server.handler;

import com.threewater.message.RpcRequestMessage;
import com.threewater.message.RpcResponseMessage;
import com.threewater.server.service.HelloService;
import com.threewater.server.service.ServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/26/10:47
 * @Description: Rpc 请求消息处理器
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) {
        // 反射调用 根据类找到它的方法并调用
        RpcResponseMessage response = new RpcResponseMessage();
        // 别忘记设置响应消息的编号
        response.setSequenceId(msg.getSequenceId());
        try {
            HelloService service = (HelloService) ServiceFactory.getService(Class.forName(msg.getInterFaceName()));
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object invoke = method.invoke(service, msg.getParameterValue());
            response.setReturnValue(invoke);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            String message = e.getCause().getMessage();
            response.setExceptionValue(new Exception("远程调用出错:" + message));
        }
        ctx.writeAndFlush(response);
    }

    /*public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        RpcRequestMessage msg = new RpcRequestMessage(
                1,
                "com.threewater.server.service.HelloService",
                "sayHello",
                String.class,
                new Class[]{String.class},
                new Object[]{"张三"}
        );
        // 根据接口找到它的实现类
        HelloService service = (HelloService) ServiceFactory.getService(Class.forName(msg.getInterFaceName()));
        Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
        Object invoke = method.invoke(service, msg.getParameterValue());
        System.out.println(invoke);
    }*/
}
