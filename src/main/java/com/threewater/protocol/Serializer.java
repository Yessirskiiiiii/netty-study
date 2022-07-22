package com.threewater.protocol;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import com.google.gson.Gson;
import com.threewater.message.LoginRequestMessage;
import com.threewater.message.LoginResponseMessage;
import com.threewater.message.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/21/14:39
 * @Description: 用于扩展序列化，反序列化算法
 */
public interface Serializer {

    // 反序列化方法
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    // 序列化方法
    <T> byte[] serialize(T object);

}
