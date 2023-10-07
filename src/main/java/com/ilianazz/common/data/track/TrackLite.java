package main.java.com.ilianazz.common.data.track;

import java.io.Serial;
import java.io.Serializable;

public class TrackLite implements Serializable {
	@Serial
	private static final long serialVersionUID = -5472946973924149998L;
	private String name;
	
    public TrackLite(final String name) {
    	this.name = name;
    }

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}
}
