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
    private Map<UserLite, ClientHandler> users;
    
    private ObjectOutputStream  out; 
    
    public ClientHandler(Socket socket, Map<UserLite, ClientHandler> users) {
        this.socket = socket;
        this.users = users;
        
        try {
			this.out = new ObjectOutputStream (socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void sendAll(final SocketMessage message) {
    	this.users.forEach((uuid, clientHandler) -> {
    		clientHandler.send(message);
    	});
    	
    }
    
    public void sendAllExclude(final SocketMessage message, final UUID excluded) {
    	System.out.println("Server: send message to all registered users excluded : " + excluded);
    	this.users.forEach((user, clientHandler) -> {
    		if (excluded != user.getUuid()) {
    			clientHandler.send(message);
    			System.out.println("Server: sending to:" + user.getUuid());
    		}
    		
    	});
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
					
					if (receivedMessage.messageType == SocketMessagesTypes.USER_CONNECT) {
						UserLite userLite = (UserLite) receivedMessage.object ;
					   	this.users.put(userLite, this);
					   	System.out.println("Server: new client : "+ userLite.getUuid() + " ! there is now " + this.users.size() + " registered clients");
						this.sendAllExclude(receivedMessage, userLite.getUuid());
						
						this.users.forEach((registeredUser, handler) -> {
							System.out.println("Server: Notifying this new client with all current clients");
							if (registeredUser.getUuid() != userLite.getUuid()) {
								SocketMessage m = new SocketMessage(SocketMessagesTypes.USER_CONNECT, registeredUser);
								this.send(m);
								System.out.println("Server: sending user: " + registeredUser.getUuid());
							}
						});
						
					}
					
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