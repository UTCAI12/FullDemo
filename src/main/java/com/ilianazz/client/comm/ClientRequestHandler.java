package main.java.com.ilianazz.client.comm;

import main.java.com.ilianazz.common.data.Model;
import main.java.com.ilianazz.common.data.track.TrackLite;
import main.java.com.ilianazz.common.data.user.UserLite;

import java.util.ArrayList;

public class ClientRequestHandler {
    private final Model model;
    public ClientRequestHandler(final Model model) {
        this.model = model;
    }

    void userConnect(final UserLite userLite) {
        this.model.addUser(userLite);
    }

    void userConnected(final ArrayList<UserLite> usersLite) {
        this.model.addUsers(usersLite);
    }

    void userDisconnect(final UserLite userLite) {
        this.model.removeUser(userLite);
    }

    void publishTrack(final TrackLite trackLite) {
        this.model.addTrack(trackLite);
    }
}
