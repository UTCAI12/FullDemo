package com.ilianazz.ai12poc.client.ihm;


import javax.swing.*;


import com.ilianazz.ai12poc.client.comm.SocketClient;
import com.ilianazz.ai12poc.common.data.Model;
import com.ilianazz.ai12poc.common.data.ModelUpdateTypes;
import com.ilianazz.ai12poc.common.data.track.TrackLite;
import com.ilianazz.ai12poc.common.data.user.UserLite;
import com.ilianazz.ai12poc.common.server.SocketMessagesTypes;

import java.nio.charset.Charset;
import java.util.Random;

public class Frame extends JFrame {
	private Model model;

	private SocketClient comm;
	final JTextArea usersList;
	public Frame(final Model model, final SocketClient comm) {
		this.model = model;
		this.comm = comm;
		
		this.setSize(1920/3, 1080/4);
		final JPanel pane = new JPanel();
		final JLabel name = new JLabel(this.model.me.getPseudo());
		name.setSize(200, 100);

		// "List of track"
		final JButton b = new JButton("Send \"new track\"");
		b.addActionListener(e -> {
			// generating random string
			String trackName = new Random().ints(97, 122 + 1)
					.limit(5)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString();

			final TrackLite newTrackLite = new TrackLite(trackName);
			Frame.this.model.addTrack(newTrackLite);
			comm.sendServer(SocketMessagesTypes.PUBLISH_TRACK, newTrackLite);
		});
		final JTextArea trackLists = new JTextArea("Tracks:\n");
		this.model.addListener(newTrackLite -> {
			trackLists.setText(trackLists.getText() + "\n-" + newTrackLite.getName());
		}, TrackLite.class, ModelUpdateTypes.NEW_TRACK);


		// Creating the textarea, all users will be listed in
		this.usersList = new JTextArea ();

		// If the model triggers an "NEW_USER" event, call the function "updateAllUsers"
		this.model.addListener(newUser -> this.updateAllUsers(), UserLite.class, ModelUpdateTypes.NEW_USER);

		// If the model triggers an "DELETE_USER" event, call the function "updateAllUsers"
		this.model.addListener(newUser -> this.updateAllUsers(), UserLite.class, ModelUpdateTypes.DELETE_USER);

		// initialize the content with all users in the model
		this.updateAllUsers();

		pane.add(name);
		pane.add(b);
		pane.add(this.usersList);
		pane.add(trackLists);
		
		this.add(pane);
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void updateAllUsers() {
		this.usersList.setText("Online users : ");
		this.model.others.forEach(user -> {
			this.usersList.setText(this.usersList.getText() + " -" + user.getPseudo() + "\n");
		});
	}

}
