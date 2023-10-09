package main.java.com.ilianazz.client.comm;

import main.java.com.ilianazz.common.data.Model;
import main.java.com.ilianazz.common.data.track.TrackLite;
import main.java.com.ilianazz.common.data.user.UserLite;
import main.java.com.ilianazz.common.server.SocketMessage;
import main.java.com.ilianazz.common.server.SocketMessagesTypes;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;


public class ClientCommunicationController implements CommunicationManager  {

	private final ClientRequestHandler clientRequestHandler;

	private final String serverAddress;
	private final int serverPort;
	private Socket socket;

	private ClientSocketManager clientSocketManager;

    public ClientCommunicationController(final String serverAddress, final int serverPort, final Model model) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.clientRequestHandler = new ClientRequestHandler(model);
    }

	public void onMessage(final SocketMessage message, final ClientSocketManager sender) {
		switch (message.messageType) {
			case USER_CONNECT -> {
				// If type is USER_CONNECT, we can get the object from the message, we know that it's a UserLite, so must be cast into UserLite
				UserLite userLiteConnected = (UserLite) message.object;

				// call method
				this.clientRequestHandler.userConnect(userLiteConnected);
			}
			case USER_CONNECTED -> {
				ArrayList<UserLite> users = (ArrayList<UserLite>) message.object;
				this.clientRequestHandler.userConnected(users);
			}
			case USER_DISCONNECT -> {
				UserLite userLiteDisconnected = (UserLite) message.object;
				this.clientRequestHandler.userDisconnect(userLiteDisconnected);
			}
			case PUBLISH_TRACK -> {
				TrackLite trackLite = (TrackLite) message.object;
				this.clientRequestHandler.publishTrack(trackLite);
			}

			default ->
				System.out.println("Unhandled message");
		}
	}

	@Override
	public void connect(final UserLite userLite) {
		this.sendServer(SocketMessagesTypes.USER_CONNECT, userLite);
	}

	public void sendServer(SocketMessagesTypes messageType, Serializable object) {
		SocketMessage message = new SocketMessage(messageType, object);
		this.clientSocketManager.send(message);
	}


    public void start() {
		try  {
			this.socket =  new Socket(serverAddress, serverPort);
			this.clientSocketManager = new ClientSocketManager(this.socket, this);
			this.clientSocketManager.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
