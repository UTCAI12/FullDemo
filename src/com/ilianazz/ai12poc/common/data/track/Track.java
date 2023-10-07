package com.ilianazz.ai12poc.common.data.track;

import java.io.File;
import java.io.Serializable;

public class Track extends TrackLite implements Serializable {
    private File mp3File;

    public Track(final String name, final File mp3File) {
        super(name);
        this.mp3File = mp3File;
    }

    public File getMp3File() {
        return mp3File;
    }
}
