package com.threewater.message;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/21/14:22
 * @Description: 心跳检测消息
 */
public class PingMessage extends Message {

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
