package java.com.cracker.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static void demo1() throws IOException {
        System.out.println("==客户端启动==");
        //1.创建一个socket通信通道，请求与服务器的端口连接
        Socket socket = new Socket("127.0.0.1", 8888);
        //2.从通道中获得一个字节输出流
        OutputStream os = socket.getOutputStream();
        //3.把字节流改装成自己需要的流进行数据的发送
        PrintStream ps = new PrintStream(os);
        //4.开始发送信息
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        while (!line.equals("exit")) {
            ps.println("我是客户端: " + line);
            ps.flush();
            line = in.nextLine();
        }
    }

    public static void main(String[] args) {
        try {
            // 1.建议一个与服务端的Socket对象：套接字
            Socket socket = new Socket("127.0.0.1", 8888);
            // 2.从socket管道中获取一个输出流，写数据给服务端
            OutputStream os = socket.getOutputStream();
            // 3.把输出流包装成一个打印流
            PrintWriter pw = new PrintWriter(os);
            // 4.反复接收用户的输入
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            while((line = br.readLine()) != null){
                pw.println(line);
                pw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void demo2() {
        new Thread(() -> {
            try {
                demo1();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                demo1();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
