package main.java.com.ilianazz.client.comm;

import main.java.com.ilianazz.common.data.Model;
import main.java.com.ilianazz.common.data.track.TrackLite;
import main.java.com.ilianazz.common.data.user.UserLite;
import main.java.com.ilianazz.common.server.SocketMessage;
import main.java.com.ilianazz.common.server.SocketMessagesTypes;

import java.util.ArrayList;

public class RequestHandler {
    private final Model model;
    public RequestHandler(final Model model) {
        this.model = model;
    }

    void handleMessage(final SocketMessage message) {
        switch (message.messageType) {
            case USER_CONNECT -> {
                // If type is USER_CONNECT, we can get the object from the message, we know that it's a UserLite, so must be cast into UserLite
                UserLite userLiteConnected = (UserLite) message.object;

                // Updating the model. Model will emit message because data has changed
                this.model.addUser(userLiteConnected);
            }
            case USER_CONNECTED -> {
                ArrayList<UserLite> users = (ArrayList<UserLite>) message.object;
                this.model.addUsers(users);
            }
            case USER_DISCONNECT -> {
                UserLite userLiteDisconnected = (UserLite) message.object;
                this.model.removeUser(userLiteDisconnected);
            }
            case PUBLISH_TRACK -> {
                TrackLite trackLite = (TrackLite) message.object;
                this.model.addTrack(trackLite);
            }
            default ->
                System.out.println("Unhandled message");
        }
    }
}
