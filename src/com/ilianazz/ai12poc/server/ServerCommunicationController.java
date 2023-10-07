package com.ilianazz.ai12poc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.ilianazz.ai12poc.common.data.user.UserLite;

public class Server {
    private int serverPort;
    
    private Map<UserLite, ClientHandler> users;
    
    public Server(final int serverPort) {
    	this.serverPort = serverPort;
    	this.users = new HashMap<UserLite, ClientHandler>();
    }
    
    public void start() {
    	try (ServerSocket serverSocket = new ServerSocket(this.serverPort)) {
            System.out.println("Server: Server started on port " + this.serverPort);
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, users).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8000);
        server.start();
    }
}   

