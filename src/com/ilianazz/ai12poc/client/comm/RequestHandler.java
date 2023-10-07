package com.ilianazz.ai12poc.client.comm;

import com.ilianazz.ai12poc.common.data.Model;
import com.ilianazz.ai12poc.common.data.track.TrackLite;
import com.ilianazz.ai12poc.common.data.user.UserLite;
import com.ilianazz.ai12poc.common.server.SocketMessage;
import com.ilianazz.ai12poc.common.server.SocketMessagesTypes;

import java.util.ArrayList;

public class RequestHandler {
    private Model model;
    public RequestHandler(final Model model) {
        this.model = model;
    }

    void handleMessage(final SocketMessage message) {
        switch (message.messageType) {
            case USER_CONNECT:
                // If type is USER_CONNECT, we can get the object from the message, we know that it's a UserLite, so must be cast into UserLite
                UserLite userLiteConnected = (UserLite) message.object;

                // Updating the model. Model will emit message because data has changed
                this.model.addUser(userLiteConnected);
                break;

            case USER_CONNECTED:
                ArrayList<UserLite> users = (ArrayList<UserLite>) message.object;
                this.model.addUsers(users);
                break;

            case USER_DISCONNECT:
                UserLite userLiteDisconnected = (UserLite) message.object;
                this.model.removeUser(userLiteDisconnected);
                break;

            case PUBLISH_TRACK:
                TrackLite trackLite = (TrackLite) message.object;
                this.model.addTrack(trackLite);
                break;

            default:
                System.out.println("Unhandled message");
                break;
        }

        // When a new message is received, checking the type,
        if (message.messageType == SocketMessagesTypes.USER_CONNECT) {


        } else if (message.messageType == SocketMessagesTypes.USER_DISCONNECT) {
            // same idea when user disconnected

        } else if (message.messageType == SocketMessagesTypes.PUBLISH_TRACK) {

        } else if (message.messageType == SocketMessagesTypes.USER_CONNECTED) {

        } else {

        }

    }
}
