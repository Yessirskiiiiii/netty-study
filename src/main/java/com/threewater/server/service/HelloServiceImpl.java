package com.threewater.server.service;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/26/11:23
 * @Description:
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
        // int a = 1 / 0;
        return "你好" + msg;
    }
}
