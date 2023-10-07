package com.ilianazz.ai12poc.client;

import com.ilianazz.ai12poc.client.comm.SocketClient;
import com.ilianazz.ai12poc.common.data.Model;
import com.ilianazz.ai12poc.common.data.ModelUpdateTypes;
import com.ilianazz.ai12poc.common.data.Track;
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
		
		final UserLite userLite = new UserLite(UUID.randomUUID(), names[index]);
		final Model m = new Model(userLite);
		
		final SocketClient comm = new SocketClient("localhost", 8000, m);
		new Thread(comm::start).start(); 
		comm.connect(userLite);
		
		
		m.addBehavior((newTrack) -> {
			comm.sendServer(SocketMessagesTypes.PUBLISH_TRACK, newTrack);
		}, Track.class, ModelUpdateTypes.NEW_TRACK);
		
		final Frame frame = new Frame(m);
	}


	public static void main(String[] args) {
		final App app = new App();
	}
}
