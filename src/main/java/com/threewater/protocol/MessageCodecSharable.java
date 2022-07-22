package com.threewater.protocol;

import com.threewater.Config;
import com.threewater.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/19/10:11
 * @Description: 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list) throws Exception {
        ByteBuf out = channelHandlerContext.alloc().buffer();
        // 1. 4 字节的魔数
        out.writeBytes(new byte[]{(byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe});
        // 2. 1 字节的版本
        out.writeByte(1);
        // 3. 1 字节的序列化方式 0：jdk 1：json
        out.writeByte(Config.getSerializerAlgorithm().ordinal());
        // 4. 1 字节的指令类型
        out.writeByte(message.getMessageType());
        // 5. 4 字节的请求序号
        out.writeInt(message.getSequenceId());
        // 1 字节，无意义，对齐填充，保证长度为 2 的次方
        out.writeByte(0xff);
        // 6. 获取内容的字节数组
        byte[] bytes = Config.getSerializerAlgorithm().serialize(message);
        // 7. 长度
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);
        list.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializationAlgorithm = in.readByte(); // 0 或 1
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        // 找到反序列化时的算法
        Algorithm algorithm = Algorithm.values()[serializationAlgorithm];
        // 确定具体消息类型
        Class<?> messageClass = Message.getMessageClass(messageType);
        Message message = (Message) algorithm.deserialize(messageClass, bytes);
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializationAlgorithm, messageType, sequenceId, length);
        log.debug("{}", message);
        list.add(message);
    }
}
