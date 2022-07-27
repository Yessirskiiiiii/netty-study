package com.threewater.protocol;

import com.google.gson.*;
import com.threewater.utils.GsonClassCodec;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/26/16:23
 * @Description:
 */
public class TestGson {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new GsonClassCodec()).create();
        System.out.println(gson.toJson(String.class));
    }

}
