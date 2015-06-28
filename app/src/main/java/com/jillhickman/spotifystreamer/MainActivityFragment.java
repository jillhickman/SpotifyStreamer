package com.jillhickman.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private  ArrayAdapter<String> mResultAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Create and array list of artist search results
        String[] data = {
                "Coldplay",
                "U2",
                "Beatles and the yellow Submarine",
                "Smashing Pumpkins",
                "Journey",
                "The Smiths",
                "Coldplay and Lele",
                "Various artist tribute",
                "Michael Jackson",
                "They might be giants"
        };
        List<String> searchResults =  new ArrayList<String>(Arrays.asList(data));

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mResultAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_searchresult, // The name of the layout ID.
                        R.id.list_item_text, // The ID of the textview to populate.
                        searchResults);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Get reference to the ListView, and attach this adapter to it
        ListView listView = (ListView) rootView.findViewById(R.id.listview_search);
        listView.setAdapter(mResultAdapter);

        //Using setOndEditorActionListener to listen for search actionSearch
        //Getting the artist_search_edittext
        //By making the editText final, I can use this inside inner classes.
        final EditText editText = (EditText) rootView.findViewById(R.id.artist_search_edittext);

        //Taking the editText and listening for input.
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Context context = getActivity();
                    //Gets the text from the editText, so can use Spotify for search
                    CharSequence artistName = editText.getText();

                    //Hides the keyboard after the enter key is pressed.
                    // reference from http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    handled = true;
                    new FindArtistTask().execute(artistName.toString());
                }
                return handled;
            }
        });
        return rootView;

    }
    //Set up AsyncTask (to start after the user execute the search)
    private class FindArtistTask extends AsyncTask<String, Integer, List<Artist> > {

        //member variable so that we can acess it outside of success method block
        private List<Artist> mArtistList;

        @Override
        protected List<Artist> doInBackground(String... params) {

            String artistName = params[0];
            //Getting the handle for the SpotifyApi
            SpotifyApi api = new SpotifyApi();

            // Most (but not all) of the Spotify Web API endpoints require authorization.
            // If you know you'll only use the ones that don't require authorisation you can skip this step
            // api.setAccessToken("myAccessToken");

            //Getting the Spotify Service
            SpotifyService spotify = api.getService();

//            spotify.searchArtists(artistName, new Callback<ArtistsPager>() {
//                @Override
//                public void success(ArtistsPager artistsPager, Response response) {
//
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//
//                }
//            });

            //Executing the query, searching for artist
            spotify.searchArtists(artistName);
            ArtistsPager j = spotify.searchArtists(artistName);
            mArtistList = j.artists.items;

//            spotify.searchArtists(artistName, new Callback<ArtistsPager>() {
//                @Override
//                public void success(ArtistsPager artistsPager, Response response) {
//                    Log.d("ArtistsPager success", artistsPager.toString());
//
//                    //Getting the list array from the artistPager, so I can
//                    //get artist name, SpotifyId, artist thumbnail image.
//                    mArtistList = artistsPager.artists.items;
//                    int n =0;
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Log.d("ArtistsPager failure", error.toString());
//                    //Toast to show that search failed in order to alert the user.
//                    int duration = Toast.LENGTH_LONG;
//                    Toast toast = Toast.makeText(getActivity(), "Artist cannot be found, Please search again", duration);
//                    toast.show();
//                }
//            });
            return mArtistList;
        }

//        @Override
        //After doInBackground, call this method to update the view
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);
            mResultAdapter.clear();

            //Do a for loop to extract the artist name & artist thumbnail image from List<Artist>
//            for (Artist artist : artists){
//
//            }

        }

    }

}
