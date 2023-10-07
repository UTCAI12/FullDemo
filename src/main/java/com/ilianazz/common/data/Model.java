package main.java.com.ilianazz.common.data;



import main.java.com.ilianazz.common.data.track.TrackLite;
import main.java.com.ilianazz.common.data.user.UserLite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model {
	public UserLite me;
	public List<UserLite> others;
	public List<TrackLite> trackLites;
	
    private final List<ListenerData<?>> listeners = new ArrayList<>();

    public Model(final UserLite userLite) {
    	this.me = userLite;
    	this.others = new ArrayList<>();
    	this.trackLites = new ArrayList<>();
	}
    
    public void addTrack(final TrackLite trackLite) {
    	this.trackLites.add(trackLite);
        this.notify(trackLite, TrackLite.class, ModelUpdateTypes.NEW_TRACK);
    }

    public void removeTrack(final TrackLite trackLite) {
    	this.trackLites.remove(trackLite);
        this.notify(trackLite, TrackLite.class, ModelUpdateTypes.DELETE_TRACK);
    }

    public void addUser(final UserLite userLite) {
    	this.others.add(userLite);
        this.notify(userLite, UserLite.class, ModelUpdateTypes.NEW_USER);
    }

    public void addUsers(final ArrayList<UserLite> users) {
        this.others.addAll(users);
        this.notify(users, ArrayList.class, ModelUpdateTypes.NEW_USERS);
    }

    public void removeUser(final UserLite userLite) {
    	this.others.remove(userLite);
        this.notify(userLite, UserLite.class, ModelUpdateTypes.DELETE_USER);
    }

    public <T extends Serializable> void addListener(UpdateListener<T> listener, Class<T> clazz, ModelUpdateTypes type) {
        this.listeners.add(new ListenerData<>(listener, clazz, type));
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> void notify(T value, Class<T> clazz, ModelUpdateTypes type) {
        this.listeners.stream()
                .filter(listener -> listener.type.equals(type))
                .filter(listener -> listener.clazz.equals(clazz))
                .map(listener -> (UpdateListener<T>) listener.listener)
                .forEach(listener -> listener.onUpdate(value));
    }

    class ListenerData<T extends Serializable> {
    	final public UpdateListener<T> listener;
    	final public Class<T> clazz;
    	final public ModelUpdateTypes type;
    	public ListenerData(final UpdateListener<T> listener, final Class<T> clazz, final ModelUpdateTypes type) {
    		this.listener = listener;
    		this.clazz = clazz;
    		this.type = type;
    	}
    }
}
