package main.java.com.ilianazz.client.hmi;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import main.java.com.ilianazz.client.comm.SocketClient;
import main.java.com.ilianazz.common.data.Model;
import main.java.com.ilianazz.common.data.ModelUpdateTypes;
import main.java.com.ilianazz.common.data.track.TrackLite;
import main.java.com.ilianazz.common.data.user.UserLite;
import main.java.com.ilianazz.common.server.SocketMessagesTypes;

import java.util.Random;

public class Controller {
    private final Model model;
    private final SocketClient comm;

    @FXML // Adding this annotation (@) means that the Node after will be injected by the FXML Loader.
    private Button buttonAddTrack; // !! The name of the variable (buttonAddTrack) MUST be the same that the name defined in sceneBuilder for this node (fx:id="buttonAddTrack")

    @FXML
    private Text username;

    @FXML
    private TextArea onlineUsersList;

    @FXML
    private TextArea trackList;

    public Controller(final Model model, final SocketClient comm) {
        this.model = model;
        this.comm = comm;
    }

    /**
     * This method must be called AFTER the initialization of the FXML file. Listeners can't be set before because FXML Nodes will be null.
     */
    void initListeners() {
        this.buttonAddTrack.setOnAction((e) -> {
            // generating random string
			String trackName = new Random().ints(97, 122 + 1)
					.limit(5)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString();

            // Creating new "track"
			final TrackLite newTrackLite = new TrackLite(trackName);

            // adding track to local model
			this.model.addTrack(newTrackLite);

            // Notifying server that a track has been added
			comm.sendServer(SocketMessagesTypes.PUBLISH_TRACK, newTrackLite);
        });


        // When an event "ModelUpdateTypes.NEW_TRACK" is sent by the server, get the title of this track and display it to "trackList"
        // The function (1st argument) takes in parameter the new track, so we are able to get its name with track.getName()
        this.model.addListener(newTrackLite ->
            trackList.appendText("-" + newTrackLite.getName() + "\n")
		, TrackLite.class, ModelUpdateTypes.NEW_TRACK);


        // If the model triggers an "NEW_USER" event, call the "updateAllUsers" method
		this.model.addListener(newUser -> this.updateUsersView(), UserLite.class, ModelUpdateTypes.NEW_USER);

		// If the model triggers an "DELETE_USER" event, call the "updateAllUsers" method
		this.model.addListener(newUser -> this.updateUsersView(), UserLite.class, ModelUpdateTypes.DELETE_USER);

		// initialize the content with all users in the model by calling the "updateAllUsers" method
		this.updateUsersView();

        // set the username
        this.username.setText(this.model.me.getPseudo());
    }

    private void updateUsersView() {
        // Unlike the "new track", here we clear the displayed content and iterate over all users to recreate the text for each one
        // in order to be able to call this function, no matters if it's delete, create or update user
        this.onlineUsersList.setText("");
        this.model.others.forEach(user ->
            this.onlineUsersList.setText(this.onlineUsersList.getText() + " -" + user.getPseudo() + "\n")
        );
    }


}

