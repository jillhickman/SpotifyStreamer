package com.jillhickman.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTenTracksActivityFragment extends Fragment {

    public TopTenTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new FindTopTenTrack().execute();



        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    //Set up AsyncTask (to start after the user taps on an artist)
    private class FindTopTenTrack extends AsyncTask<String, Integer, Tracks > {

        //member variable so that we can access it
        private Tracks mTracksList;

        @Override
        protected Tracks doInBackground(String... params) {

            //Set toTenArtist to the DataRepo topTenTrackArtist.
            Artist topTenArtist = DataRepo.topTenTrackArtist;
            //Set topTenArtistId by drilling into topTenArtist and getting the id.
            //Need this Id to start a query for top tracks query.
            String topTenArtistId = topTenArtist.id;


            //Getting the handle for the SpotifyApi
            SpotifyApi api = new SpotifyApi();

            //Getting the Spotify Service
            SpotifyService spotify = api.getService();


            //Getting the Map with the key value pairs.
            Map<String, Object> options = new HashMap<>();

            //Setting the options country to US.
            options.put("country", "US");

            //Executing the query, searching for Tracks
            //Searches and gets tracks
            Tracks tracks = spotify.getArtistTopTrack(topTenArtistId, options);

            //Sets the mTracksList with the result of the tracks searched
            mTracksList = tracks;


            return mTracksList;
        }

        //After doInBackground, call this method to update the view
        @Override
        protected void onPostExecute(Tracks tracks) {
            //If tracks is empty or tracks is null, display toast. No tracks
            //were found for the artist.
            if ( tracks.tracks.isEmpty()|| tracks.tracks == null){
                Context context = getActivity();

                Toast.makeText(context, R.string.toast_message, Toast.LENGTH_LONG).show();

            }
            //If tracks is not empty, display results
            else if(tracks.tracks != null) {

                //Instead of adding to the adapter, I added outside of the fragment life cycle
                //so that the fragment does not blow away my data on screen rotation.
                //Clear array list from the DataRepo, to clear previous search results.
                //Add the array list from the DataRepo.
                //Update the adapter that the data has changed.
                DataRepo.tracks.tracks.clear();
                DataRepo.tracks.tracks.addAll(tracks.tracks);
//                mResultAdapter.notifyDataSetChanged();

            }
            super.onPostExecute(tracks);

        }

    }



}
