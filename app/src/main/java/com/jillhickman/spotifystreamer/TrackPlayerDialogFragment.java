package com.jillhickman.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by jillhickman on 7/27/15.
 */
public class TrackPlayerDialogFragment extends DialogFragment{


    public static final String TAG = "TrackPlayerDialogFragment";

    //Making it a member variable because this holds a songTrack data to display in UI
    public Track mSongTrack;


    static TrackPlayerDialogFragment newInstance() {
        TrackPlayerDialogFragment f = new TrackPlayerDialogFragment();
        return f;
    }

    //Inflate the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        int position = mArgs.getInt("song");

        //Get handle to the Tracks list
        Tracks allTracks = DataRepo.trackListHolder;
        //Getting the handle for the Track obj in the appropriate position
        mSongTrack= allTracks.tracks.get(position);

        View v = inflater.inflate(R.layout.trackplayerlayout, container, false);
        return v;
    }

}

