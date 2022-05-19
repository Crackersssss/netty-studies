package java.com.cracker.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static void demo1() throws IOException {
        System.out.println("==服务端启动==");
        //1.注册端口
        ServerSocket serverSocket = new ServerSocket(8888);
        //2.开始在这里暂停等待接受客户端的连接，得到一个端到端的socket通道
        Socket socket = serverSocket.accept();
        //3.从socket管道得到一个字节输入流
        InputStream is = socket.getInputStream();
        //4.把子节输入流包装成自己需要的流进行数据读取
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        //5.读取数据
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("服务端收到: " + line);
        }
    }

    private static void demo2() throws IOException {
        System.out.println("==服务端启动==");
        //1.注册端口
        ServerSocket serverSocket = new ServerSocket(8888);
        while (true) {
            //2.开始在这里暂停等待接收客户端的连接,得到一个端到端的Socket管道
            Socket socket = serverSocket.accept();
            new ServerReadThread(socket).start();
            System.out.println(socket.getRemoteSocketAddress() + "上线了");
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("----服务端启动----");
            ServerSocket ss = new ServerSocket(8888);
            //一个服务端只需要对应一个线程池
            HandlerSocketThreadPool pool = new HandlerSocketThreadPool(3, 1000);

            //客户端可能有多个
            while (true) {
                Socket socket = ss.accept();
                System.out.println("有人上线了！");
                pool.execute(new ReaderClientRunnable(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

class ReaderClientRunnable implements Runnable {

    private final Socket socket;

    public ReaderClientRunnable(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 读取一行数据
            InputStream is = socket.getInputStream() ;
            // 转成一个缓冲字符流
            Reader fr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(fr);
            // 一行一行的读取数据
            String line = null ;
            while((line = br.readLine())!=null){ // 阻塞式的！！
                System.out.println("服务端收到了数据："+line);
            }
        } catch (Exception e) {
            System.out.println("有人下线了");
        }

    }
}

class ServerReadThread extends Thread {

    private final Socket socket;

    public ServerReadThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //3.从Socket管道中得到一个字节输入流。
            InputStream is = socket.getInputStream();
            //4.把字节输入流包装成自己需要的流进行数据的读取。
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //5.读取数据
            String line ;
            while((line = br.readLine())!=null){
                System.out.println("服务端收到："+socket.getRemoteSocketAddress()+":"+line);
            }
        } catch (Exception e) {
            System.out.println(socket.getRemoteSocketAddress() + "下线了");
        }
    }
}
