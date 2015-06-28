package com.jillhickman.spotifystreamer;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by jillhickman on 6/27/15.
 */
public class SpotifyArtistAdapter extends ArrayAdapter<Artist> {
    public SpotifyArtistAdapter(Context context, int resource, List<Artist> objects) {
        super(context, resource, objects);
    }
}
