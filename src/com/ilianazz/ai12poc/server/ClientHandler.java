package com.ilianazz.ai12poc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

import com.ilianazz.ai12poc.common.data.user.UserLite;
import com.ilianazz.ai12poc.common.server.SocketMessage;
import com.ilianazz.ai12poc.common.server.SocketMessagesTypes;

public class ClientHandler extends Thread {

    private final Socket socket;
    private ObjectOutputStream  out;

	private UserLite user;
	private ServerCommunicationController serverController;

    public ClientHandler(final Socket socket, final ServerCommunicationController serverController) {
        this.serverController = serverController;
		this.socket = socket;

        try {
			this.out = new ObjectOutputStream (socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public UserLite getUser() {
		return this.user;
	}

	public void setUser(final UserLite user) {
		this.user = user;
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
					System.out.println("Server: received "+ receivedMessage);
					
					this.serverController.onMessage(receivedMessage, this);
					
				} catch (java.net.SocketException e) {
					// If user disconnect without sending a DISCONNECT message, create one
					final SocketMessage socketMessage = new SocketMessage(SocketMessagesTypes.USER_DISCONNECT, this.user);
					this.serverController.onMessage(socketMessage, this);
					return;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public void start() {
    	System.out.println("Server: New socket");
    	super.start();
    }
    
    
}