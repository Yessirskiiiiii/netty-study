package com.threewater.utils;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/26/17:25
 * @Description: Gson 自定义序列化适配器，适配 Class 类型数据
 */
public class GsonClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

    @Override
    public Class<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // json -> class
        try {
            String str = jsonElement.getAsString();
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Class<?> aClass, Type type, JsonSerializationContext jsonSerializationContext) {
        // class -> json
        return new JsonPrimitive(aClass.getName());
    }

}
