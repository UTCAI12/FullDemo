package main.java.com.ilianazz.common.data.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class UserLite implements Serializable {
	@Serial
	private static final long serialVersionUID = 271472610567368097L;
	private UUID uuid;
	private String pseudo;
	
	public UserLite(final UUID uuid, String pseudo) {
		this.uuid = uuid;
		this.pseudo = pseudo;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(final UUID uuid) {
		this.uuid = uuid;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(final String pseudo) {
		this.pseudo = pseudo;
	}
	
	
}
