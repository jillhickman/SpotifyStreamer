package com.jillhickman.spotifystreamer;

import android.content.Context;
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
import android.widget.Toast;

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
                    CharSequence text = editText.getText();
                    int duration = Toast.LENGTH_SHORT;

                    //Hides the keyboard after the enter key is pressed.
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);


                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    handled = true;
                }
                return handled;
            }
        });

        return rootView;
    }
}
