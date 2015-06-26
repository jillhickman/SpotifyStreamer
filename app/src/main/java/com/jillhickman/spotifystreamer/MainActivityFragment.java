package com.jillhickman.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

        return rootView;
    }
}
