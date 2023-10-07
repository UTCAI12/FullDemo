package com.ilianazz.ai12poc.client.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ilianazz.ai12poc.common.data.Model;
import com.ilianazz.ai12poc.common.data.user.UserLite;
import com.ilianazz.ai12poc.common.server.SocketMessage;
import com.ilianazz.ai12poc.common.server.SocketMessagesTypes;


public class SocketClient {

    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Model model;
    
    public SocketClient(String serverAddress, int serverPort, Model model) {
    	this.model = model;
    	try {
			this.socket = new Socket(serverAddress, serverPort);
			this.out = new ObjectOutputStream(socket.getOutputStream());
	    	this.in =  new ObjectInputStream(socket.getInputStream());
	    	
	    	
    	} catch (UnknownHostException e) {
            System.err.println("Unknown host: " + this.serverAddress);
        } catch (IOException e) {
            System.err.println("Couldn't connect to: " + this.serverPort);
        }
    }
    	
    
    public void sendServer(SocketMessagesTypes messageType, Serializable object) {
    	SocketMessage message = new SocketMessage(messageType, object);
    	try {
			this.out.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public void connect(final UserLite userLite) {
    	this.sendServer(SocketMessagesTypes.USER_CONNECT, userLite);
    }
    
    public void start() {
    	System.out.println("Client: start lisening");
		while (true) {
		    try {
				SocketMessage message = (SocketMessage) this.in.readObject();
				System.out.println("Client: User receved new message" + message);
				
				if (message.messageType == SocketMessagesTypes.USER_CONNECT) {
					UserLite userLite = (UserLite) message.object;
					this.model.addUser(userLite);
				}
		    	
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}