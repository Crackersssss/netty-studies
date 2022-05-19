package com.cracker.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class TestClient {

    public static void client() {
        try {
            //
            SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
            //
            sChannel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Scanner in = new Scanner(System.in);
            while (in.hasNext()) {
                String str = in.nextLine();
                buffer.put((new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(System.currentTimeMillis())
                        + "\n" + str
                ).getBytes());
                buffer.flip();
                sChannel.write(buffer);
                buffer.clear();
            }
            sChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        client();
    }
}
