package main.java.Journal_Management_System.server;
import main.java.Journal_Management_System.util.DatabaseConnection;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening on " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("�ͻ���������");

                    // ʹ�� DatabaseConnection �������ȡ���ݿ�����
                    Connection connection = DatabaseConnection.getCon();

                    // Ϊÿ���ͻ������Ӵ���һ���µ��߳�
                    ServerThread serverThread = new ServerThread(clientSocket, connection);
                    new Thread(serverThread).start();
                } catch (Exception e) {
                    System.out.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 9999; // �����������Ķ˿ں�
        Server server = new Server(port);
        server.start();
    }
}
