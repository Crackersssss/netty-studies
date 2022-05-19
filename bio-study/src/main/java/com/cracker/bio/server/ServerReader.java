package com.cracker.bio.server;

import com.cracker.bio.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Set;

public class ServerReader extends Thread {

    private final Socket socket;

    public ServerReader(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(socket.getInputStream());
            while (true) {
                int flag = dis.readInt();
                if (1 == flag) {
                    String name = dis.readUTF();
                    System.out.println(name + "---->" + socket.getRemoteSocketAddress());
                    ServerChat.getOnlineSockets().put(socket, name);
                }
                writeMsg(flag, dis);
            }
        } catch (Exception e) {
            System.out.println("--有人下线了--");
            ServerChat.getOnlineSockets().remove(socket);
            try {
                writeMsg(1, dis);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void writeMsg(final int flag, final DataInputStream dis) throws Exception {
        //
        String msg = null;
        if (1 == flag) {
            //
            StringBuilder rs = new StringBuilder();
            Collection<String> onlineNames = ServerChat.getOnlineSockets().values();
            //
            if (onlineNames != null && !onlineNames.isEmpty()) {
                onlineNames.forEach(each -> rs.append(each + Constants.SPILIT));
            }
            //
            msg = rs.substring(0, rs.lastIndexOf(Constants.SPILIT));
            //
            sendMsgToAll(flag, msg);
        } else if (2 == flag || 3 == flag) {
            //
            String newMsg = dis.readUTF();
            //
            String sendName = ServerChat.getOnlineSockets().get(socket);
            //
            StringBuilder msgFinal = new StringBuilder();
            //
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss: EEE");
            if (2 == flag) {
                msgFinal.append(sendName).append(" ").append(sdf.format(System.currentTimeMillis())).append("\r\n");
                msgFinal.append(" ").append(newMsg).append("\r\n");
                sendMsgToAll(flag, msgFinal.toString());
            } else if (3 == flag) {
                msgFinal.append(sendName).append(" ").append(sdf.format(System.currentTimeMillis())) .append("对你私发\r\n");
                msgFinal.append(" ").append(newMsg).append("\r\n");
            }
            //
            String destName = dis.readUTF();
            sendMsgToOne(destName, msgFinal.toString());
        }
    }

    private void sendMsgToAll(final int flag, final String msg) {
        //
        Set<Socket> allOnlineSockets = ServerChat.getOnlineSockets().keySet();
        allOnlineSockets.forEach(each -> {
            try {
                DataOutputStream dos = new DataOutputStream(each.getOutputStream());
                //
                dos.writeInt(flag);
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void sendMsgToOne(final String destName, final String msg) {
        Set<Socket> allOnLineSockets = ServerChat.getOnlineSockets().keySet();
        allOnLineSockets.forEach(each -> {
            if (ServerChat.getOnlineSockets().get(each).trim().equals(destName)) {
                try {
                    DataOutputStream dos = new DataOutputStream(each.getOutputStream());
                    dos.writeInt(2);
                    dos.writeUTF(msg);
                    dos.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
