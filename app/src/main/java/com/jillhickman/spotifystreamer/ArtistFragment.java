package com.jillhickman.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.jillhickman.spotifystreamer.Events.NewTopTenTracksEvents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistFragment extends Fragment {

    private SpotifyArtistAdapter mResultAdapter;

    private ListView mListView;

    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    public ArtistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Get the handle on the empty data array from SpotifyStreamerApplication class
        List starterList = SpotifyStreamerApplication.artists;

        // Create an ArrayAdapter, it will take data from a source and
        // use it to populate the ListView it's attached to.
        mResultAdapter =
                new SpotifyArtistAdapter(
                        getActivity(), // The current context (this activity)
                        starterList);//On start up, there is no object

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Get reference to the ListView, and attach this adapter to it
        mListView = (ListView) rootView.findViewById(R.id.listview_search);
        mListView.setAdapter(mResultAdapter);

        //Get handle for searchView
        final SearchView mSearchText = (SearchView) rootView.findViewById(R.id.searchView);

        //IconifiedByDefault is set to false so that the search box show with search hint.
        mSearchText.setIconifiedByDefault(false);

        //Taking the searchView and setOnQueryListener.
        mSearchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean handled = false;

                Context context = getActivity();

                //Gets the text from the searchView, so can use Spotify for search
                CharSequence artistName = mSearchText.getQuery();

                //Searching the spotify API through AsyncTask.
                new FindArtistTask().execute(artistName.toString());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Use setItemClickListener to show detail view when click on an artist
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Getting the handle for the Artist obj in the appropriate position
                //Accessing the SpotifyStreamerApplication because that's where the artists list of artist objects.
                Artist artist = SpotifyStreamerApplication.artists.get(position);

                //Setting the artist that I got from the SpotifyStreamerApplication.artists to the topTenTrackArtist
                //so that I can access the artist for subtitle and track info query.
                SpotifyStreamerApplication.topTenTrackArtist = artist;

                new FindTopTenTrack().execute();

                //If it is a tablet, show the top ten tracks when list item is clicked by
                // adding the detail fragment using a fragment transaction.
                if (SpotifyStreamerApplication.tablet == true) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.track_detail_container, new TopTenTracksActivityFragment(), DETAILFRAGMENT_TAG)
                            .commit();

                    //For phone, launch an intent for new activity
                } else {
                    //Explicit intent to take to TopTenTracksActivity
                    Intent intent = new Intent(getActivity(), TopTenTracksActivity.class);
                    startActivity(intent);
                }
            }
        });

        return rootView;

    }

    //Set up AsyncTask (to start after the user execute the search)
    private class FindArtistTask extends AsyncTask<String, Integer, List<Artist>> {

        //member variable so that we can access it
        private List<Artist> mArtistList;

        @Override
        protected List<Artist> doInBackground(String... params) {

            //Start the task in the first position
            String artistName = params[0];

            //Getting the handle for the SpotifyApi
            SpotifyApi api = new SpotifyApi();

            //Getting the Spotify Service
            SpotifyService spotify = api.getService();


            //Executing the query, searching for artist
            //Searches and gets artist, added a try catch for Retrofit error.
            //Toast message to alert the user that there was a problem
            try {
                ArtistsPager artistsPager = spotify.searchArtists(artistName);

                //Sets the mArtistList with the result of the artist searched
                mArtistList = artistsPager.artists.items;
            } catch (RetrofitError retrofitError) {
                //Used runOnUiThread when doing toast from background thread
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), R.string.error_message, Toast.LENGTH_LONG).show();
                    }
                });
            }
            return mArtistList;
        }

        //After doInBackground, call this method to update the view
        @Override
        protected void onPostExecute(List<Artist> artists) {
            //If artists is empty or artist is null, display toast
            //If no artist is found, lets the user know
            if (artists == null ||artists.isEmpty()) {
                Context context = getActivity();

                Toast.makeText(context, R.string.toast_message, Toast.LENGTH_LONG).show();

            }
            //If artists is not empty, display results
            else if (artists != null) {

                //Instead of adding to the adapter, I added outside of the fragment life cycle
                //so that the fragment does not blow away my data on screen rotation.
                //Clear array list from the SpotifyStreamerApplication, to clear previous search results.
                //Add the array list from the SpotifyStreamerApplication.
                //Update the adapter that the data has changed.
                SpotifyStreamerApplication.artists.clear();
                SpotifyStreamerApplication.artists.addAll(artists);
                mResultAdapter.notifyDataSetChanged();

            }
            super.onPostExecute(artists);
        }
    }

    //Set up AsyncTask (to start after the user taps on an artist)
    private class FindTopTenTrack extends AsyncTask<String, Integer, Tracks> {

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
            Tracks tracks;
            try {
                tracks = spotify.getArtistTopTrack(topTenArtistId, options);
            } catch (RetrofitError e) {
                e.printStackTrace();
                return null;
            }


            return tracks;
        }

        //After doInBackground, call this method to update the view
        @Override
        protected void onPostExecute(Tracks tracks) {
            //If tracks is null or If trackListHolder is empty or trackListHolder is null, display toast. No trackListHolder
            //were found for the artist.
            if (tracks == null || tracks.tracks == null || tracks.tracks.isEmpty()){
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

                //Posting this because top ten fragment needs to know that it has new data to show.
                SpotifyStreamerApplication.ottoBus.post(new NewTopTenTracksEvents());
            }
            super.onPostExecute(tracks);

        }
    }

}
