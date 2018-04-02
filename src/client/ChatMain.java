package client;

import java.io.*;
import java.net.Socket;

public class ChatMain {
    private String ipAddressServer;
    private String port = "8082";
    String nickname;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public static void main(String[] args) {
        ChatMain chatController = new ChatMain();
        StartWin startWin = new StartWin();
        startWin.init(chatController);
    }

    void startConnection(String ipAddressServer){
        this.ipAddressServer = ipAddressServer;
        ChatWin chatWin;
        try {
            socket = new Socket(this.ipAddressServer, Integer.parseInt(port));
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            chatWin = new ChatWin(socket, dataInputStream, dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
