package com.threewater;

import com.threewater.protocol.Algorithm;
import com.threewater.protocol.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/21/15:14
 * @Description:
 */
public class Config {

    static Properties properties;

    static {
        try (InputStream in = Config.class.getResourceAsStream("/applications.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getServerPort() {
        String value = properties.getProperty("server.port");
        if (value == null) {
            return 8080;
        } else {
            return Integer.parseInt(value);
        }
    }

    public static Algorithm getSerializerAlgorithm() {
        String value = properties.getProperty("serializer.algorithm");
        if (value == null) {
            return Algorithm.Java;
        } else {
            return Algorithm.valueOf(value);
        }
    }

}
