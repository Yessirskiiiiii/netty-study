package com.threewater.message;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/21/14:25
 * @Description: 心跳检测消息
 */
public class PongMessage extends Message {

    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
