package com.cracker.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TestServer {

    public static void server() {
        try {
            //打开一个通道
            ServerSocketChannel ssChannel = ServerSocketChannel.open();
            //设置为非阻塞式
            ssChannel.configureBlocking(false);
            //绑定端口
            ssChannel.bind(new InetSocketAddress(9999));
            //打开一个选择器
            Selector selector = Selector.open();
            //将选择器注册进通道
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
            //
            while (selector.select() > 0) {
                System.out.println("轮一轮");
                //
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    //
                    SelectionKey sk = it.next();
                    if (sk.isAcceptable()) {
                        //
                        SocketChannel sChannel = ssChannel.accept();
                        sChannel.configureBlocking(false);
                        sChannel.register(selector, SelectionKey.OP_READ);
                    } else if (sk.isReadable()) {
                        SocketChannel sChannel = (SocketChannel) sk.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = sChannel.read(buffer)) > 0) {
                            buffer.flip();
                            System.out.println(new String(buffer.array(), 0, len));
                            buffer.clear();
                        }
                    }
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        server();
    }
}
