package main.java.com.ilianazz.client.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import main.java.com.ilianazz.common.data.Model;
import main.java.com.ilianazz.common.data.user.UserLite;
import main.java.com.ilianazz.common.server.SocketMessage;
import main.java.com.ilianazz.common.server.SocketMessagesTypes;


public class SocketClient {

    private ObjectOutputStream out;
    private ObjectInputStream in;

	private final RequestHandler requestHandler;
    
    public SocketClient(String serverAddress, int serverPort, Model model) {

		this.requestHandler = new RequestHandler(model);

    	try (final Socket socket = new Socket(serverAddress, serverPort) ) {
			this.out = new ObjectOutputStream(socket.getOutputStream());
	    	this.in =  new ObjectInputStream(socket.getInputStream());

    	} catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverAddress);
        } catch (IOException e) {
            System.err.println("Couldn't connect to: " + serverPort);
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
    	System.out.println("Client: start listening");
		while (true) {
		    try {
				// Reading message from the socket. readObject is sync : waiting for message to be received. So this method call is blocking while no message.
				final SocketMessage message = (SocketMessage) this.in.readObject();
				System.out.println("Client: User received new message" + message);
				this.requestHandler.handleMessage(message);

			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}