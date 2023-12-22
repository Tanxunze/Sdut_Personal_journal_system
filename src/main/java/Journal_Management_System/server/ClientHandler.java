package main.java.Journal_Management_System.server;

import main.java.Journal_Management_System.util.Request;
import main.java.Journal_Management_System.util.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(String serverAddress, int serverPort) throws Exception {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.socket = new Socket(serverAddress, serverPort);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void sendRequest(Request request) throws Exception {
        out.writeObject(request);
        out.flush();
    }

    public Response receiveResponse() throws Exception {
        return (Response) in.readObject();
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
