package com.jillhickman.spotifystreamer.Events;

/**
 * Created by jillhickman on 8/5/15.
 */
public class ChangedTrackTimeEvent {

    public long mElapsedTime;

    //Constructor that takes the track time increment is played and is set to elapsed time.
    public ChangedTrackTimeEvent(long time) {
        mElapsedTime = time;
    }
}
