package com.threewater.protocol;

import com.threewater.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/18/15:51
 * @Description: 消息编解码器
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        // 1. 4 字节的魔数
        byteBuf.writeBytes(new byte[]{(byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe});
        // 2. 1 字节的版本
        byteBuf.writeByte(1);
        // 3. 1 字节的序列化方式 0：jdk 1：json
        byteBuf.writeByte(0);
        // 4. 1 字节的指令类型
        byteBuf.writeByte(message.getMessageType());
        // 5. 4 字节的请求序号
        byteBuf.writeInt(message.getSequenceId());
        // 1 字节，无意义，对齐填充，保证长度为 2 的次方
        byteBuf.writeByte(0xff);
        // 6. 获取内容的字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(message);
        byte[] bytes = bos.toByteArray();
        // 7. 长度
        byteBuf.writeInt(bytes.length);
        // 8. 写入内容
        byteBuf.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int magicNum = byteBuf.readInt();
        byte version = byteBuf.readByte();
        byte serializationAlgorithm = byteBuf.readByte();
        byte messageType = byteBuf.readByte();
        int sequenceId = byteBuf.readInt();
        byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes, 0, length);
        // if (serializationAlgorithm == 0) {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();
        // }
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializationAlgorithm, messageType, sequenceId, length);
        log.debug("{}", message);
        list.add(message);
    }
}
