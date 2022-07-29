package com.threewater.client;

import com.threewater.message.RpcRequestMessage;
import com.threewater.protocol.SequenceIdGenerator;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.Proxy;

import static com.threewater.client.handler.RpcResponseMessageHandler.PROMISES;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/29/10:22
 * @Description: 代理工厂类，创造代理对象
 */
public class ProxyFactory {

    public static <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{serviceClass};
        Object obj = Proxy.newProxyInstance(loader, interfaces, ((proxy, method, args) -> {
            // 1. 将方法调用转换为消息对象
            int sequenceId = SequenceIdGenerator.nextId();
            RpcRequestMessage msg = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
            );
            // 2. 准备一个 promise 对象，来接收结果
            DefaultPromise<Object> promise = new DefaultPromise<>(RpcClientManager.getChannel().eventLoop()); // 指定 promise 对象异步接收结果的线程
            PROMISES.put(sequenceId, promise);
            // 3. 将消息对象发送出去
            RpcClientManager.getChannel().writeAndFlush(msg);
            // 4. 等待 promise 的结果，直到 promise 接收到结果，才会继续向下进行
            promise.await();
            if (promise.isSuccess()) {
                // 正常
                return promise.getNow();
            } else {
                // 失败
                throw new RuntimeException(promise.cause());
            }
        }));
        return (T) obj;
    }

}
