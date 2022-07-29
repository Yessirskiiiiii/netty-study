package com.threewater.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/29/10:30
 * @Description: 单机计数器
 */
public class SequenceIdGenerator {

    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }

}
