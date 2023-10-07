package main.java.com.ilianazz.server;

import main.java.com.ilianazz.common.data.track.TrackLite;
import main.java.com.ilianazz.common.data.user.UserLite;
import main.java.com.ilianazz.common.server.SocketMessage;
import main.java.com.ilianazz.common.server.SocketMessagesTypes;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class RequestHandler {

    private final Map<UserLite, ClientHandler> users;
    public RequestHandler(final Map<UserLite, ClientHandler> users) {
        this.users = users;
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


    void userConnect(final SocketMessage message, final UserLite userLite, final ClientHandler sender) {
        // update the local model with the new user
        this.users.put(userLite, sender);

        // associate the ClientHandler with the appropriate User
        sender.setUser(userLite);

        System.out.println("Server: new client : "+ userLite.getUuid() + " ! there is now " + this.users.size() + " registered clients");

        // Notify all users exclude "user" in parameter that a new user is connected (just forwarding the initial message)
        this.sendAllExclude(message, userLite.getUuid());

        // TODO : remove multiple message sending, send only one global message : SocketMessagesTypes.USER_CONNECTED
        SocketMessage m = new SocketMessage(SocketMessagesTypes.USER_CONNECTED, new ArrayList<>(users.keySet()));
        sender.send(m);

        /*
        this.users.forEach((registeredUser, handler) -> {
            System.out.println("Server: Notifying this new client with all current clients");
            if (registeredUser.getUuid() != userLite.getUuid()) {
                SocketMessage m = new SocketMessage(SocketMessagesTypes.USER_CONNECT, registeredUser);
                System.out.println("Server: sending user: " + registeredUser.getUuid());
            }
        });
        */
    }

    void userDisconnect(final SocketMessage message, final UserLite userLite, final ClientHandler sender) {
        // removing the user from the local "model"
        this.users.remove(userLite);
        // notifying others users by forwarding the initial message
        this.sendAllExclude(message, userLite.getUuid());
    }


    void publishTrack(final SocketMessage message, final TrackLite trackLite, final ClientHandler sender) {
        // each sender (ClientSocketHandler) has a user associated, forwarding the new track
        this.sendAllExclude(message, sender.getUser().getUuid());
    }
}
