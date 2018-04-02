package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWin extends JFrame {

    public void init(ChatMain chatController) {
        setSize(250, 300);
        setTitle("Подключение");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JLabel ipAddressLabel = new JLabel("IP-адрес сервера:");
        JLabel nicknameLabel = new JLabel("Ваше имя:");
        JLabel chatLabel = new JLabel("Чат ждет тебя");
        chatLabel.setHorizontalAlignment(JLabel.CENTER);
        JTextField ipAddressField = new JTextField("localhost");
        JTextField nicknameField = new JTextField("Anonymous");
        JButton button = new JButton("Go!");
        JPanel panel = new JPanel();
//        setLayout(new FlowLayout());
        setLayout(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        //setLayout(new BoxLayout( BoxLayout.PAGE_AXIS));
        panel.add(ipAddressLabel);
        panel.add(ipAddressField);
        panel.add(nicknameLabel);
        panel.add(nicknameField);
        //panel.add(button);
//        add(ipAddressLabel);
//        add(ipAddressField);
//        add(nicknameLabel);
//        add(nicknameField);
//        add(button);
        add(panel, BorderLayout.NORTH);
        add(chatLabel, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
        final StartWin startWin = this;
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatController.startConnection(ipAddressField.getText());
                startWin.dispose();

            }
        });

        setVisible(true);

    }
}
