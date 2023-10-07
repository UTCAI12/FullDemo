package com.ilianazz.ai12poc.client;

import com.ilianazz.ai12poc.client.comm.SocketClient;
import com.ilianazz.ai12poc.common.data.Model;
import com.ilianazz.ai12poc.common.data.ModelUpdateTypes;
import com.ilianazz.ai12poc.common.data.track.TrackLite;
import com.ilianazz.ai12poc.common.data.user.UserLite;
import com.ilianazz.ai12poc.client.ihm.Frame;
import com.ilianazz.ai12poc.common.server.SocketMessagesTypes;

import java.util.Random;
import java.util.UUID;



public class App {
	public App() {
		
		String[] names = {
				"Ilian",
				"Clement",
				"Myriem",
				"Gwendal",
				"Matthieu",
				"Cecile",
				"Mostafa",
				"Sophie",
				"Riyad",
				"Valentin",
				"Edouard",
				"Thomas" };
		Random random = new Random();
		int index = random.nextInt(names.length);

		// Initializing a user with a random name
		final UserLite userLite = new UserLite(UUID.randomUUID(), names[index]);

		// Initializing the Model
		final Model m = new Model(userLite);

		// Initializing the communication
		final SocketClient comm = new SocketClient("localhost", 8000, m);

		// Starting the socket in another thread
		new Thread(comm::start).start();

		// Connect to the server
		comm.connect(userLite);
		
		final Frame frame = new Frame(m, comm);
	}


	public static void main(String[] args) {
		final App app = new App();
	}
}
