package main.java.com.ilianazz.server;

import main.java.com.ilianazz.common.data.track.TrackLite;
import main.java.com.ilianazz.common.data.user.UserLite;
import main.java.com.ilianazz.common.server.SocketMessage;
import main.java.com.ilianazz.common.server.SocketMessagesTypes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

public class ServerCommunicationController {
    private final int serverPort;

    private final Map<SocketMessagesTypes, BiConsumer<SocketMessage, ServerSocketManager>> messageHandlers;

    private final ServerRequestHandler serverRequestHandler;
    private final Map<UserLite, ServerSocketManager> users;

    public ServerCommunicationController(final int serverPort) {
    	this.serverPort = serverPort;
    	this.users = new HashMap<>();
        this.serverRequestHandler = new ServerRequestHandler(users);

        this.messageHandlers = new HashMap<>();
        // Associez les types de message aux méthodes correspondantes de clientHandler
        messageHandlers.put(SocketMessagesTypes.USER_CONNECT, (message, sender) -> {
            serverRequestHandler.userConnect(message, (UserLite) message.object, sender);
        });
        messageHandlers.put(SocketMessagesTypes.USER_DISCONNECT, (message, sender) -> {
            serverRequestHandler.userDisconnect(message, (UserLite) message.object, sender);
        });
        messageHandlers.put(SocketMessagesTypes.PUBLISH_TRACK, (message, sender) -> {
            serverRequestHandler.publishTrack(message, (TrackLite) message.object, sender);
        });
    }

    /**
     * This method is called by the ClientHandler (instance how receives messages from the socket of a specific client
     * @param message The message send by the client
     * @param sender The Socket how sent the message
     */
    public void onMessage(final SocketMessage message, final ServerSocketManager sender) {
        // getting the method associated to the message type
        BiConsumer<SocketMessage, ServerSocketManager> handler = messageHandlers.get(message.messageType);

        if (handler != null) {
            // if handler is not null, means that a method is defined
            handler.accept(message, sender);
        } else {
            // if handler is null, no function for this message type
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

