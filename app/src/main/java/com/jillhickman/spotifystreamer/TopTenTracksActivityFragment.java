package com.jillhickman.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTenTracksActivityFragment extends Fragment {

    private SpotifyTracksAdapter mTrackResultAdapter;

    private Tracks mTracksList;

    public TopTenTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new FindTopTenTrack().execute();

        //Get the handle on the data array from SpotifyStreamerApplication class
        List starterList = SpotifyStreamerApplication.trackListHolder.tracks;

        // Create an ArrayAdapter, it will take data from a source and
        // use it to populate the ListView it's attached to.
        mTrackResultAdapter =
                new SpotifyTracksAdapter(
                        getActivity(), // The current context (this activity)
                        starterList);//On start up, should have result of top ten tracks

        View rootView= inflater.inflate(R.layout.fragment_detail, container, false);

        //Get reference to the ListView, and attach this adapter to it
        ListView listView = (ListView) rootView.findViewById(R.id.listview_tracks);
        listView.setAdapter(mTrackResultAdapter);

        //Use setOnItemClickListener to show dialog fragment when click on track
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Getting the position of the track from the SpotifyStreamerApplication
                SpotifyStreamerApplication.positionOfTrack = position;

                //Get instance of the dialog fragment and show with fragment manager.
                DialogFragment newFragment = new TrackPlayerDialogFragment();
                //Show the new fragment by passing in the activity and fragment manager with Tag.
                newFragment.show(getActivity().getSupportFragmentManager(), TrackPlayerDialogFragment.TAG);
            }
        });
        return rootView;
    }

    //Set up AsyncTask (to start after the user taps on an artist)
    private class FindTopTenTrack extends AsyncTask<String, Integer, Tracks > {

        @Override
        protected Tracks doInBackground(String... params) {

            //Set toTenArtist to the SpotifyStreamerApplication topTenTrackArtist.
            Artist topTenArtist = SpotifyStreamerApplication.topTenTrackArtist;
            //Set topTenArtistId by drilling into topTenArtist and getting the id.
            //Need this Id to start a query for top trackListHolder query.
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
            //Searches and gets trackListHolder
            Tracks tracks = spotify.getArtistTopTrack(topTenArtistId, options);

            //Sets the mTracksList with the result of the trackListHolder searched
            mTracksList = tracks;

            return mTracksList;
        }

        //After doInBackground, call this method to update the view
        @Override
        protected void onPostExecute(Tracks tracks) {
            //If trackListHolder is empty or trackListHolder is null, display toast. No trackListHolder
            //were found for the artist.
            if ( tracks.tracks.isEmpty()|| tracks.tracks == null){
                Context context = getActivity();

                Toast.makeText(context, R.string.track_toast_message, Toast.LENGTH_LONG).show();
            }
            //If trackListHolder is not empty, display results
            else if(tracks.tracks != null) {

                //Instead of adding to the adapter, I added outside of the fragment life cycle
                //so that the fragment does not blow away my data on screen rotation.
                //Clear array list from the SpotifyStreamerApplication, to clear previous search results.
                //Add the array list from the SpotifyStreamerApplication.
                //Update the adapter that the data has changed.
                SpotifyStreamerApplication.trackListHolder.tracks.clear();
                SpotifyStreamerApplication.trackListHolder.tracks.addAll(tracks.tracks);
                mTrackResultAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(tracks);

        }

    }


}
