package com.cracker.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

public class TestBuffer {

    @Test
    public void test3() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println(buffer.isDirect());
    }

    @Test
    public void test2() {
        String str = "cracker";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        buffer.flip();
        byte[] dst = new byte[buffer.limit()];
        buffer.get(dst, 0, 2);
        System.out.println(new String(dst, 0, 2));
        System.out.println(buffer.position());
        //标记
        buffer.mark();
        buffer.get(dst, 2, 2);
        System.out.println(new String(dst, 2, 2));
        System.out.println(buffer.position());
        //恢复到标记处
        buffer.reset();
        System.out.println(buffer.position());
        if (buffer.hasRemaining()) {
            System.out.println(buffer.remaining());
        }
    }

    @Test
    public void test1() {
        String str = "cracker";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("----allocate----");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        buffer.put(str.getBytes());
        System.out.println("----put----");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        buffer.flip();
        System.out.println("----flip()----");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        byte[] dst = new byte[buffer.limit()];
        buffer.get(dst);
        System.out.println(new String(dst, 0, dst.length));

        System.out.println("-----get()----");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        //rewind: 可重复读
        buffer.rewind();
        System.out.println("----rewind()----");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        buffer.clear();
        System.out.println("----clear()----");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        System.out.println((char)buffer.get());
    }
}
