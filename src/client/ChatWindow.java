package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

public class ChatWindow extends JFrame implements Runnable {

    private final Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private final JTextArea outTextArea;
    private final JTextField inTextField;

    public ChatWindow(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream, String nickname) {
        //super("Client");
        super(nickname);
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;

        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        outTextArea = new JTextArea();
        add(outTextArea);
        inTextField = new JTextField();
        add(BorderLayout.SOUTH, inTextField);
        final ChatWindow chat = this;
        //todo scrolling required

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                try {
                    chat.dataOutputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    chat.socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        inTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chat.dataOutputStream.writeUTF(inTextField.getText());
                    chat.dataOutputStream.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                inTextField.setText("");
            }
        });

        setVisible(true);
        inTextField.requestFocus();
        new Thread(this).start();
    }


//    public static void oldmain(String[] args) {
//        String site = "localhost";
//        String port = "8082";
//
//        Socket socket = null;
//        DataInputStream dataInputStream = null;
//        DataOutputStream dataOutputStream = null;
////        new client.ChatWindow(null, null, null);
////        StartDialog startWin = new StartDialog();
////        startWin.init();
//        try {
//            socket = new Socket(site, Integer.parseInt(port));
//            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//            dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//            new ChatWindow(socket, dataInputStream, dataOutputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//            try {
//                if (dataOutputStream != null) {
//                    dataOutputStream.close();
//                }
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            try {
//                if (socket != null) {
//                    socket.close();
//                }
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }

    @Override
    public void run() {
        try {
            while (true) { // todo flag
                String line = dataInputStream.readUTF();
                outTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inTextField.setVisible(false);
            validate();
        }
    }
}
