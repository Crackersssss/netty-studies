package com.cracker.bio.client;

import com.cracker.bio.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientChat implements ActionListener {

    //
    private final JFrame win = new JFrame();

    //
    protected JTextArea smsContent = new JTextArea(23, 50);

    //
    private JTextArea smsSend = new JTextArea(4, 40);

    //
    public JList<String> onlineUsers = new JList<>();

    //
    private JCheckBox isPrivateBn = new JCheckBox("私聊");

    //
    private JButton sendBn = new JButton("发送");

    //
    private JFrame loginView;

    private JTextField ipEt, nameEt, idEt;

    private Socket socket;

    private void initView() {
        //
        win.setSize(650, 600);
        //
        displayLoginView();
        //
        //displayChatView();
    }

    private void displayChatView() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        //
        win.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(smsSend);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(sendBn);
        btns.add(isPrivateBn);
        bottomPanel.add(btns, BorderLayout.EAST);
        //
        smsContent.setBackground(new Color(0xdd, 0xdd, 0xdd));
        //
        win.add(new JScrollPane(smsContent), BorderLayout.CENTER);
        smsContent.setEditable(false);
        //
        Box rightBox = new Box(BoxLayout.Y_AXIS);
        onlineUsers.setFixedCellHeight(120);
        onlineUsers.setVisibleRowCount(13);
        rightBox.add(new JScrollPane(onlineUsers));
        win.add(rightBox, BorderLayout.EAST);
        //
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.pack();
        //
        setWindowCenter(win, 650, 600, true);
        //
        sendBn.addActionListener(this);
    }

    private void displayLoginView() {
        //
        loginView = new JFrame("登录");
        loginView.setLayout(new GridLayout(3, 1));
        loginView.setSize(400, 200);
        JPanel ip = new JPanel();
        JLabel label = new JLabel("    IP:");
        ip.add(label);
        ipEt = new JTextField(20);
        ip.add(ipEt);
        loginView.add(ip);
        //
        JPanel name = new JPanel();
        JLabel label1 = new JLabel("姓名:");
        name.add(label1);
        nameEt = new JTextField(20);
        name.add(nameEt);
        loginView.add(name);

        JPanel btnView = new JPanel();
        JButton login = new JButton("登陆");
        btnView.add(login);
        JButton cancle = new JButton("取消");
        btnView.add(cancle);
        loginView.add(btnView);
        //
        loginView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setWindowCenter(loginView,400,260,true);
        //
        login.addActionListener(this);
        cancle.addActionListener(this);
    }

    private static void setWindowCenter(JFrame frame, int width , int height, boolean flag) {
        //
        Dimension ds = frame.getToolkit().getScreenSize();
        //
        int width1 = ds.width;
        //
        int height1 = ds.height ;
        System.out.println(width1 +"*" + height1);
        //
        frame.setLocation(width1/2 - width/2, height1/2 -height/2);
        frame.setVisible(flag);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //
        JButton btn = (JButton) e.getSource();
        switch (btn.getText()) {
            case "登陆":
                String ip = ipEt.getText().toString();
                String name = nameEt.getText().toString();
                //
                String msg = "";
                //
                if(ip==null || !ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
                    msg = "请输入合法的服务端ip地址";
                }else if(name==null || !name.matches("\\S{1,}")){
                    msg = "姓名必须1个字符以上";
                }
                if(!msg.equals("")){
                    // msg有内容说明参数有为空
                    // 参数一：弹出放到哪个窗口里面
                    JOptionPane.showMessageDialog(loginView, msg);
                }else{
                    try {
                        // 参数都合法了
                        // 当前登录的用户,去服务端登陆
                       // 先把当前用户的名称展示到界面
                        win.setTitle(name);
                        // 去服务端登陆连接一个socket管道
                        socket = new Socket(ip, Constants.PORT);

                        //为客户端的socket分配一个线程 专门负责收消息
                        new ClientReader(this,socket).start();

                        // 带上用户信息过去
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        dos.writeInt(1); // 登录消息
                        dos.writeUTF(name.trim());
                        dos.flush();

                        // 关系当前窗口 弹出聊天界面
                        loginView.dispose(); // 登录窗口销毁
                        displayChatView(); // 展示了聊天窗口了


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                break;
                case "取消":
                    // 退出系统
                    System.exit(0);
                    break;
                case "发送":
                    // 得到发送消息的内容
                    String msgSend = smsSend.getText().toString();
                    if(!msgSend.trim().equals("")){
                        // 发消息给服务端
                        try {
                            // 判断是否对谁发消息
                            String selectName = onlineUsers.getSelectedValue();
                            int flag = 2 ;// 群发 @消息
                            if(selectName!=null&&!selectName.equals("")){
                                msgSend =("@"+selectName+","+msgSend);
                                // 判断是否选中了私发
                                if(isPrivateBn.isSelected()){
                                    // 私发
                                    flag = 3 ;//私发消息
                                }
                            }
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeInt(flag); // 群发消息  发送给所有人
                            dos.writeUTF(msgSend);
                            if(flag == 3){
                                // 告诉服务端我对谁私发
                                dos.writeUTF(selectName.trim());
                            }
                            dos.flush();

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    smsSend.setText(null);
                    break;
        }
    }

    public static void main(String[] args) {
        new ClientChat().initView();
        new ClientChat().initView();
        new ClientChat().initView();
    }
}
