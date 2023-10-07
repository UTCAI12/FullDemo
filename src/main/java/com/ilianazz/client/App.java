package main.java.com.ilianazz.client;

import main.java.com.ilianazz.client.comm.SocketClient;
import main.java.com.ilianazz.client.hmi.Controller;
import main.java.com.ilianazz.common.data.Model;
import main.java.com.ilianazz.common.data.user.UserLite;
import main.java.com.ilianazz.client.hmi.Frame;
import javafx.application.Application;

import java.util.Random;
import java.util.UUID;



public class App {
	public App(String[] args) {
		
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
		
		final Controller controller = new Controller(m, comm);

		// Set the controller in MyApp
		Frame.setController(controller);

		// Launch the application
		Application.launch(Frame.class, args);
	}


	public static void main(String[] args) {
		new App(args);
	}
}
