package main.java.Journal_Management_System.server;

import main.java.Journal_Management_System.dao.UserDAO;
import main.java.Journal_Management_System.util.Request;
import main.java.Journal_Management_System.util.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;

public class ServerThread implements Runnable {
    private Socket socket;
    private UserDAO userDAO;

    public ServerThread(Socket socket, Connection connection) {
        this.socket = socket;
        this.userDAO = new UserDAO(connection);
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            Request request = (Request) input.readObject();
            Response response = new Response();

            // 处理登录请求
            if ("login".equals(request.getRequestType())) {
                String username = (String) request.getData().get("username");
                String password = (String) request.getData().get("password");

                if (userDAO.validateUser(username, password)) {
                    response.setStatusCode(200);
                    String role = userDAO.getUserRole(username);
                    response.addData("role", role);
                    response.setMessage("Login successful");
                } else {
                    response.setStatusCode(401);
                    response.setMessage("Invalid username or password");
                }
            }

            // 处理注册请求
            else if ("register".equals(request.getRequestType())) {
                String username = (String) request.getData().get("username");
                String password = (String) request.getData().get("password");

                if (userDAO.addUser(username, password)) {
                    response.setStatusCode(200);
                    response.setMessage("Registration successful");
                } else {
                    response.setStatusCode(500);
                    response.setMessage("Registration failed");
                }
            }

            output.writeObject(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
