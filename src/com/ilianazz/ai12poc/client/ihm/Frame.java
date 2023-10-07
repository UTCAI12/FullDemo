package com.ilianazz.ai12poc.client.ihm;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import com.ilianazz.ai12poc.common.data.Model;
import com.ilianazz.ai12poc.common.data.ModelUpdateTypes;
import com.ilianazz.ai12poc.common.data.Track;
import com.ilianazz.ai12poc.common.data.user.UserLite;

public class Frame extends JFrame {
	private Model model;
	
	public Frame(final Model model) {
		this.model = model;
		
		this.setSize(1920/3, 1080/4);
				
		
		final JPanel pane = new JPanel();
		final JLabel name = new JLabel(this.model.me.getPseudo());
		name.setSize(200, 100);
		final JButton b = new JButton("click");
		b.addActionListener(e -> {
			final Track newTrack = new Track(name.getText());
			Frame.this.model.addTrack(newTrack);
		});
		
		
		final JLabel allUsers = new JLabel("Online users : ");
		this.model.others.forEach(user -> {
			allUsers.setText(allUsers.getText() + " -" + user.getPseudo() + ", ");
		});
		
		this.model.addBehavior(newUser -> {
			allUsers.setText(allUsers.getText() + " -" + newUser.getPseudo() + ", ");
		}, UserLite.class, ModelUpdateTypes.NEW_USER);
		
		
		pane.add(name);
		pane.add(b);
		pane.add(allUsers);
		
		this.add(pane);
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
}
