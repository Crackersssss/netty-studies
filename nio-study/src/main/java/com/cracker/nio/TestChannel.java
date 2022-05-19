package com.cracker.nio;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestChannel {

    @Test
    public void copy() throws Exception {
        File srcFile = new File("D:\\Users\\Cracker\\Desktop\\StreamAPI.png");
        File destFile = new File("D:\\Users\\Cracker\\Desktop\\StreamAPI-copy.png");
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        //得到文件通道
        FileChannel isChannel = fis.getChannel();
        FileChannel osChannel = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            //必须先清空缓存区
            buffer.clear();
            int flag = isChannel.read(buffer);
            if (flag == -1) {
                break;
            }
            buffer.flip();
            osChannel.write(buffer);
        }
        isChannel.close();
        osChannel.close();
        System.out.println("copy successfully!");
    }
}
