package com.ilianazz.ai12poc.common.data;



import com.ilianazz.ai12poc.common.data.user.UserLite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model {
	public UserLite me;
	public List<UserLite> others;
	public List<Track> tracks;
	
    private final List<BehaviorData<?>> behaviors = new ArrayList<>();
    
    
    public Model(final UserLite userLite) {
    	this.me = userLite;
    	this.others = new ArrayList<>();
    	this.tracks = new ArrayList<>();
	}
    
    public void addTrack(final Track track) {
    	this.tracks.add(track);
        this.notify(track, Track.class, ModelUpdateTypes.NEW_TRACK);
    }

    public void removeTrack(final Track track) {
    	this.tracks.remove(track);
        this.notify(track, Track.class, ModelUpdateTypes.DELETE_TRACK);
    }

    public void addUser(final UserLite userLite) {
    	this.others.add(userLite);
        this.notify(userLite, UserLite.class, ModelUpdateTypes.NEW_USER);
    }

    public void removeUser(final UserLite userLite) {
    	this.others.remove(userLite);
        this.notify(userLite, UserLite.class, ModelUpdateTypes.DELETE_USER);
    }

    public <T extends Serializable> void addBehavior(UpdateBehavior<T> behavior, Class<T> clazz, ModelUpdateTypes type) {
        this.behaviors.add(new BehaviorData<>(behavior, clazz, type));
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> void notify(T value, Class<T> clazz, ModelUpdateTypes type) {
        this.behaviors.stream()
                .filter(behavior -> behavior.type.equals(type))
                .filter(behavior -> behavior.clazz.equals(clazz))
                .map(behavior -> (UpdateBehavior<T>) behavior.behavior)
                .forEach(behavior -> behavior.onUpdate(value));
    }

    class BehaviorData<T extends Serializable> {
    	final public UpdateBehavior<T> behavior;
    	final public Class<T> clazz;
    	final public ModelUpdateTypes type;
    	public BehaviorData(final UpdateBehavior<T> behavior, final Class<T> clazz, final ModelUpdateTypes type) {
    		this.behavior = behavior;
    		this.clazz = clazz;
    		this.type =type;
    	}
    }
}
