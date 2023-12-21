package main.java.Journal_Management_System.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                ServerThread serverThread = new ServerThread(socket);
                new Thread(serverThread).start();
            }

        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 1234; // 可以是任何未被占用的端口号
        Server server = new Server(port);
        server.start();
    }
}

