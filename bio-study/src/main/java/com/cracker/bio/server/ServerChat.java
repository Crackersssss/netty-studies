package com.cracker.bio.server;

import com.cracker.bio.utils.Constants;
import lombok.Getter;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerChat {

    @Getter
    private static Map<Socket, String> onlineSockets = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerReader(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
