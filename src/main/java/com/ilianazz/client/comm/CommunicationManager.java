package main.java.com.ilianazz.client.comm;

import main.java.com.ilianazz.common.data.user.UserLite;

public interface CommunicationManager {
    /**
     * Call to notify to the server your user
     * @param userLite The new user public profile
     */
    void connect(final UserLite userLite);
}
