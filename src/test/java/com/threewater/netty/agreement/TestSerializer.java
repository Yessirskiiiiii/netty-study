package com.threewater.netty.agreement;

import com.threewater.message.LoginRequestMessage;
import com.threewater.protocol.MessageCodecSharable;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/21/15:47
 * @Description:
 */
public class TestSerializer {

    public static void main(String[] args) {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);

        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(message);
    }

}
