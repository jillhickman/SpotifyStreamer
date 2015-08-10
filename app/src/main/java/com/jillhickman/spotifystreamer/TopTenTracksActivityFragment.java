package com.jillhickman.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jillhickman.spotifystreamer.Events.NewTopTenTracksEvents;
import com.squareup.otto.Subscribe;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTenTracksActivityFragment extends Fragment {

    private SpotifyTracksAdapter mTrackResultAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        new FindTopTenTrack().execute();

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

    //Listens to see if new data, the adapter handles the new data
    @SuppressWarnings("unused")
    @Subscribe
    public void onNewTopTenTracksEvent (NewTopTenTracksEvents event) {
        mTrackResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Register with the bus
        SpotifyStreamerApplication.ottoBus.register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        //Unregister with the bus
        SpotifyStreamerApplication.ottoBus.unregister(this);

    }

}
