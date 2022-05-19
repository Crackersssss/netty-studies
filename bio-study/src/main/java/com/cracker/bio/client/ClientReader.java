package com.cracker.bio.client;

import com.cracker.bio.utils.Constants;

import java.io.DataInputStream;
import java.net.Socket;

public class ClientReader extends Thread {

    private final Socket socket;

    private final ClientChat clientChat;

    public ClientReader(final ClientChat clientChat, final Socket socket) {
        this.clientChat = clientChat;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            //
            while (true) {
                int flag = dis.readInt();
                if (1 == flag) {
                    //
                    String nameDatas = dis.readUTF();
                    //
                    String[] names = nameDatas.split(Constants.SPILIT);
                    clientChat.onlineUsers.setListData(names);
                } else if (2 == flag) {
                    //群发,私聊 , @消息 都是直接显示的。
                    String msg = dis.readUTF() ;
                    clientChat.smsContent.append(msg);
                    // 让消息界面滾動到底端
                    clientChat.smsContent.setCaretPosition(clientChat.smsContent.getText().length());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
