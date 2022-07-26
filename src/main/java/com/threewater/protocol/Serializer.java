package com.threewater.protocol;

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
