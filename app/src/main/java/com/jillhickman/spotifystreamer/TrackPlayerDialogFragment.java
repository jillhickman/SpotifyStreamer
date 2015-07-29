package com.jillhickman.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jillhickman on 7/27/15.
 */
public class TrackPlayerDialogFragment extends DialogFragment{


    public static final String TAG = "TrackPlayerDialogFragment";

    static TrackPlayerDialogFragment newInstance() {
        TrackPlayerDialogFragment f = new TrackPlayerDialogFragment();
        return f;
    }


    //Inflate the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.trackplayerlayout, container, false);
        return v;
    }



}

