package com.jillhickman.spotifystreamer;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Tracks;
//import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by jillhickman on 6/30/15.
 *Created this class so that the data can be separate from the view.
 * According to the model view controller pattern.
 * https://en.wikipedia.org/wiki/Model–view–controller
 * In my case, the controller is the search button on the keyboard, and the
 * view is the listview and the adapter.
 */

public class DataRepo {
    //artists obj ArrayList
    public static final List<Artist> artists = new ArrayList<>();

    //artist obj. Need so that we can get a handle for the subtitle and track query.
    public static Artist topTenTrackArtist = new Artist();

    //trackListHolder obj ArrayList
    public static final Tracks trackListHolder = new Tracks();

    //Need to initialize the trackListHolder trackListHolder that has the ArrayList
    //so that we can avoid the null pointer error.
    public static void initializeTracks(){
        trackListHolder.tracks = new ArrayList<>();
    }

}





