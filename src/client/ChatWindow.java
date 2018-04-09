package client;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
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
        super("jabachat: " + nickname);
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;

        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        outTextArea = new JTextArea();
        //outTextArea.

        inTextField = new JTextField();
        JScrollPane scrollPane = new JScrollPane(outTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        DefaultCaret caret = (DefaultCaret) outTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        //jPanel.add(outTextArea);
        jPanel.add(scrollPane);

        add(jPanel);
        JButton jButton = new JButton("Где шеф???");
        jPanel.add(BorderLayout.SOUTH, jButton);

        add(BorderLayout.SOUTH, inTextField);
        add(BorderLayout.CENTER, jPanel);

        final ChatWindow chat = this;


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
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chat.dataOutputStream.writeUTF("Где шеф???");
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

    @Override
    public void run() {
        try {
            while (true) { // todo flag
                String line = dataInputStream.readUTF();
//                char[] chars = new char[10];
//                line.getChars(0,1,chars,0);
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
