package main.java.Journal_Management_System.server;

import main.java.Journal_Management_System.util.Request;
import main.java.Journal_Management_System.util.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            Request request = (Request) input.readObject();
            // 处理请求
            // ...

            // 发送响应
            Response response = new Response();
            // 设置response对象的属性
            output.writeObject(response);

        } catch (Exception e) {
            System.out.println("Server thread exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

