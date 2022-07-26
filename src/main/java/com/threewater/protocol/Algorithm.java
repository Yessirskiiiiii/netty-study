package com.threewater.protocol;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.*;
import com.threewater.message.LoginRequestMessage;
import com.threewater.message.LoginResponseMessage;
import com.threewater.utils.GsonClassCodec;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/22/11:06
 * @Description: 序列化算法
 */
public enum Algorithm implements Serializer{

    Java {
        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                return (T) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("反序列化失败", e);
            }
        }

        @Override
        public <T> byte[] serialize(T object) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(object);
                return bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("序列化失败", e);
            }
        }
    },

    Json {
        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new GsonClassCodec()).create();
            String json = new String(bytes, StandardCharsets.UTF_8);
            return gson.fromJson(json, clazz);
        }

        @Override
        public <T> byte[] serialize(T object) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new GsonClassCodec()).create();
            String json = gson.toJson(object);
            return json.getBytes(StandardCharsets.UTF_8);
        }
    },

    Hessian {
        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            try {
                Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
                return (T) input.readObject();
            } catch (IOException e) {
                throw new RuntimeException("反序列化失败", e);
            }
        }

        @Override
        public <T> byte[] serialize(T object) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Hessian2Output output = new Hessian2Output(bos);
                output.getBytesOutputStream().flush();
                output.completeMessage();
                output.writeObject(object);
                output.close();
                return bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("序列化失败", e);
            }
        }
    },

    Kryo {
        // Kryo 可能存在线程安全问题，文档上是推荐放在 ThreadLocal 里，一个线程一个 Kryo，或者用 Kryo 自带的池
        private static final ThreadLocal<com.esotericsoftware.kryo.Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
            com.esotericsoftware.kryo.Kryo kryo = new Kryo();
/*            kryo.register(LoginRequestMessage.class);
            kryo.register(LoginResponseMessage.class);*/
            kryo.setReferences(true);
            kryo.setRegistrationRequired(false);
            return kryo;
        });

            /*final Pool<Kryo> kryoPool = new Pool<com.esotericsoftware.kryo.Kryo>(true, false, 8) {
                @Override
                protected com.esotericsoftware.kryo.Kryo create() {
                    com.esotericsoftware.kryo.Kryo kryo = new Kryo();
                    kryo.register(LoginRequestMessage.class);
                    kryo.register(LoginResponseMessage.class);
                    kryo.setReferences(true);
                    kryo.setRegistrationRequired(false);
                    return kryo;
                }
            };*/

        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            try (Input input = new Input(new ByteArrayInputStream(bytes))) {
                Kryo kryo = kryoThreadLocal.get();
                // com.esotericsoftware.kryo.Kryo kryo = kryoPool.obtain();
                T object = kryo.readObject(input, clazz);
                kryoThreadLocal.remove();
                // kryoPool.free(kryo);
                return object;
            } catch (Exception e) {
                throw new RuntimeException("反序列化失败", e);
            }
        }

        @Override
        public <T> byte[] serialize(T object) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 Output output = new Output(bos)) {
                Kryo kryo = kryoThreadLocal.get();
                // com.esotericsoftware.kryo.Kryo kryo = kryoPool.obtain();
                kryo.writeObject(output, object);
                kryoThreadLocal.remove();
                // kryoPool.free(kryo);
                return output.toBytes();
            } catch (Exception e) {
                throw new RuntimeException("序列化失败", e);
            }
        }
    } // TODO

}
