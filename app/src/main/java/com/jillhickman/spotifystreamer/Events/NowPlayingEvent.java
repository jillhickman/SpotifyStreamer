package com.jillhickman.spotifystreamer.Events;

/**
 * Created by jillhickman on 8/6/15.
 */
public class NowPlayingEvent {

    public long mMaxDuration;

    public NowPlayingEvent(long time) {
        mMaxDuration = time;
    }
}
