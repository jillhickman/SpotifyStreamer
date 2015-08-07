package com.jillhickman.spotifystreamer;

/**
 * Created by jillhickman on 8/5/15.
 *
 */
public class ChangedSeekBarEvent {

    public long mChangedSeekBar;

    //Constructor that takes the seek bar's position in the track
    public ChangedSeekBarEvent(long position) {
        mChangedSeekBar = position;
    }
}
