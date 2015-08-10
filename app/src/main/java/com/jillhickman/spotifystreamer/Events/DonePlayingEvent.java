package com.jillhickman.spotifystreamer.Events;

/**
 * Created by jillhickman on 8/7/15.
 */
public class DonePlayingEvent {

    public long mElapsedTime;
    public DonePlayingEvent(long time) {
        mElapsedTime = time;
    }
}
