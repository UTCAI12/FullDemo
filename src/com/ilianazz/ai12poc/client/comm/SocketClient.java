package com.ilianazz.ai12poc.client.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.ilianazz.ai12poc.common.data.Model;
import com.ilianazz.ai12poc.common.data.track.TrackLite;
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

	private RequestHandler requestHandler;
    
    public SocketClient(String serverAddress, int serverPort, Model model) {
    	this.model = model;
		this.requestHandler = new RequestHandler(model);
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
				// Reading message from the socket. readObject is sync : waiting for message to be received. So this method call is blocking while no message.
				final SocketMessage message = (SocketMessage) this.in.readObject();
				System.out.println("Client: User receved new message" + message);

				this.requestHandler.handleMessage(message);

			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}