package server;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatHandler extends Thread {
    private final Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private static List<ChatHandler> handlers = Collections.synchronizedList(new ArrayList<>());

    public ChatHandler(Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    @Override
    public void run() {
        handlers.add(this);
        try {
            while (true) { // todo flag
                String message = dataInputStream.readUTF();
                broadcast(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            handlers.remove(this);
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String message) {
        synchronized (handlers) {
            Iterator<ChatHandler> iterator = handlers.iterator();
            while (iterator.hasNext()) {
                ChatHandler chatHandler = iterator.next();
                try {
                    // todo DZ отдельный метод
                    synchronized (chatHandler.dataOutputStream) {
                        chatHandler.dataOutputStream.writeUTF(getTime() + ": " + message);
                    }
                    chatHandler.dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getTime() {
        Calendar calendar= Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        return dateFormat.format(date);
    }
}
