package com.threewater.netty.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/12/21:55
 * @Description: ByteBuf 的创建
 */
public class TestByteBuf {

    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(); // 默认直接内存
        System.out.println(buf); // PooledUnsafeDirectByteBuf(ridx: 0, widx: 0, cap: 256)

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            stringBuilder.append("a");
        }
        buf.writeBytes(stringBuilder.toString().getBytes());
        System.out.println(buf); // PooledUnsafeDirectByteBuf(ridx: 0, widx: 300, cap: 512)
        // ByteBuf 容量会动态改变，即动态扩容

    }

}
