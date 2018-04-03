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
        StartDialog startDialog = new StartDialog();
        startDialog.init(chatController);
    }

    void startConnection(String ipAddressServer, String nickname){
        this.ipAddressServer = ipAddressServer;
        ChatWindow chatWindow;
        try {
            socket = new Socket(this.ipAddressServer, Integer.parseInt(port));
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            dataOutputStream.writeUTF(nickname);
            chatWindow = new ChatWindow(socket, dataInputStream, dataOutputStream, nickname);
        } catch (IOException e) {
            System.out.println("Не удалось подключиться");
            //todo здесь надо окно с ошибкой
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
