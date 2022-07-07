package com.threewater.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/05/15:47
 * @Description: 两个 Channel 传输数据
 */
public class TestFileChannelTransferTo {

    public static void main(String[] args) {
        String FROM = "helloword/data.txt";
        String TO = "helloword/to.txt";
        long start = System.nanoTime();
        try (FileChannel from = new FileInputStream(FROM).getChannel();
             FileChannel to = new FileOutputStream(TO).getChannel()
        ) {
            from.transferTo(0, from.size(), to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("transferTo 用时：" + (end - start) / 1000_000.0);
    }

}
