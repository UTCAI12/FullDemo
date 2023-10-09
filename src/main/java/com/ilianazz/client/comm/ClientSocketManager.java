package main.java.com.ilianazz.client.comm;

import main.java.com.ilianazz.common.server.SocketMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketManager extends Thread {

    private final ClientCommunicationController clientController;

    private final Socket socket;
    private ObjectOutputStream  out;

    public ClientSocketManager(final Socket socket, final ClientCommunicationController clientController) {
        this.clientController = clientController;
        this.socket = socket;

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(final SocketMessage message) {
        try {
            this.out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (
            ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream())
        ) {
            while (true) {
                try {
                    SocketMessage receivedMessage = (SocketMessage) in.readObject();
                    System.out.println("Client: received "+ receivedMessage);
                    this.clientController.onMessage(receivedMessage, this);
                } catch (java.net.SocketException e) {
                    // If user disconnect without sending a DISCONNECT message, create one
                    e.printStackTrace();
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        super.start();
    }

}
