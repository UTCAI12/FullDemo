package com.ilianazz.ai12poc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ilianazz.ai12poc.common.data.track.TrackLite;
import com.ilianazz.ai12poc.common.data.user.UserLite;
import com.ilianazz.ai12poc.common.server.SocketMessage;
import com.ilianazz.ai12poc.common.server.SocketMessagesTypes;
import com.sun.net.httpserver.Request;

public class ServerCommunicationController {
    private int serverPort;

    private final RequestHandler requestHandler;
    private Map<UserLite, ClientHandler> users;
    
    public ServerCommunicationController(final int serverPort) {
    	this.serverPort = serverPort;
    	this.users = new HashMap<UserLite, ClientHandler>();
        this.requestHandler = new RequestHandler(users);
    }

    /**
     * This method is called by the ClientHandler (instance how receives messages from the socket of a specific client
     * @param message The message send by the client
     * @param sender The Socket how sent the message
     */
    public void onMessage(final SocketMessage message, final ClientHandler sender) {
        switch (message.messageType) {
            case USER_CONNECT:
                this.requestHandler.userConnect(message, (UserLite) message.object, sender);
                break;
            case USER_DISCONNECT:
                this.requestHandler.userDisconnect(message, (UserLite) message.object, sender);
                break;
            case PUBLISH_TRACK:
                this.requestHandler.publishTrack(message, (TrackLite) message.object, sender);
                break;
            default:
                System.out.println("Unhandled message");
                break;
        }

    }

    public void start() {
    	try (ServerSocket serverSocket = new ServerSocket(this.serverPort)) {
            System.out.println("Server: Server started on port " + this.serverPort);
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, this).start();
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

