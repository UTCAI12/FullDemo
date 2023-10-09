package main.java.com.ilianazz.server;

import main.java.com.ilianazz.common.data.track.TrackLite;
import main.java.com.ilianazz.common.data.user.UserLite;
import main.java.com.ilianazz.common.server.SocketMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerCommunicationController {
    private final int serverPort;

    private final ServerRequestHandler serverRequestHandler;
    private final Map<UserLite, ServerSocketManager> users;
    
    public ServerCommunicationController(final int serverPort) {
    	this.serverPort = serverPort;
    	this.users = new HashMap<>();
        this.serverRequestHandler = new ServerRequestHandler(users);
    }

    /**
     * This method is called by the ClientHandler (instance how receives messages from the socket of a specific client
     * @param message The message send by the client
     * @param sender The Socket how sent the message
     */
    public void onMessage(final SocketMessage message, final ServerSocketManager sender) {
        switch (message.messageType) {
            case USER_CONNECT ->
                this.serverRequestHandler.userConnect(message, (UserLite) message.object, sender);
            case USER_DISCONNECT ->
                this.serverRequestHandler.userDisconnect(message, (UserLite) message.object, sender);
            case PUBLISH_TRACK ->
                this.serverRequestHandler.publishTrack(message, (TrackLite) message.object, sender);
            default ->
                System.out.println("Unhandled message");
        }

    }

    public void start() {
    	try (ServerSocket serverSocket = new ServerSocket(this.serverPort)) {
            System.out.println("Server: Server started on port " + this.serverPort);
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                new ServerSocketManager(clientSocket, this).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerCommunicationController server = new ServerCommunicationController(8000);
        server.start();
    }
}   

