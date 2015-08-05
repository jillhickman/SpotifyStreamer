package com.jillhickman.spotifystreamer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by jillhickman on 7/27/15.
 */
public class TrackPlayerDialogFragment extends DialogFragment {

    public static final String TAG = "TrackPlayerDialogFragment";

    MyService mService;

    boolean mBound = false;
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.setUrl(SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).preview_url);
            mService.play();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            mBound = false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Gets rid of the title in the layout for this dialog fragment.
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.trackplayerlayout, container, false);

        //Check to see if previous or next button should be disabled
        checkPositonIsValid(v);

        //Getting the handle to the imageView
        ImageView albumArtworkView = (ImageView) v.findViewById(R.id.trackplayer_image);
        //Get the album image for the song in the 0 position.
        Image imageOfAlbumArtwork = SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).album.images.get(0);

        //Get the Url of the image
        String imageUrl = imageOfAlbumArtwork.url;
        //Set picasso to use that Url
        Picasso.with(getActivity()).load(imageUrl).into(albumArtworkView);

        //Getting the handle to the artist name textView
        TextView artistNameView = (TextView) v.findViewById(R.id.trackplayer_artist_name);
        //Getting the handle to the artist name from the SpotifyStreamerApplication at selected position
        Artist topTenArtist = SpotifyStreamerApplication.topTenTrackArtist;
        String artistName = topTenArtist.name;

        //Set textView to the artistName
        artistNameView.setText(artistName);

        //Getting the handle to the album name textView
        TextView albumNameView = (TextView) v.findViewById(R.id.trackplayer_albumn_name);
        String albumName = SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).album.name;
        //Set the textView to the album name
        albumNameView.setText(albumName);

        //Getting the handle to the track name textView
        TextView trackNameView = (TextView) v.findViewById(R.id.trackplayer_track_name);
        String trackName = SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).name;
        trackNameView.setText(trackName);

        //Getting the handle to the playButton
        final ImageButton playButton = (ImageButton) v.findViewById(R.id.trackplayer_play_button);
        //Shows pause button
        playButton.setSelected(true);

        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //If music is playing, show the pause button
                if (mService.isPlaying()) {
                    mService.pause();
                    playButton.setSelected(false);
                    playButton.setEnabled(true);
                }else {
                //If music is paused, show the play button
                    mService.resume();
                    playButton.setSelected(true);
                }


            }
        });

        //Getting the handle to the previous button
        ImageButton previousButton = (ImageButton) v.findViewById(R.id.trackplayer_previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position and decrement to previous position.
                SpotifyStreamerApplication.positionOfTrack--;
                //Check position, previous or next button should show or be disabled
                checkPositonIsValid(getView());
                goToTrack();
            }
        });

        //Getting the handle to the next button
        ImageButton nextButton = (ImageButton) v.findViewById(R.id.trackplayer_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position and increment to next position.
                SpotifyStreamerApplication.positionOfTrack++;
                //Check position, previous or next button should show or be disabled
                checkPositonIsValid(getView());
                goToTrack();
            }
        });

        //Getting the handle to the seek bar
        SeekBar seekBar = (SeekBar) v.findViewById(R.id.trackplayer_seek_bar);
        //Get the max duration of track
        long maxDuration = SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).duration_ms;
        //Set max duration of track and set to seek bar
        seekBar.setMax((int) maxDuration);


        return v;
    }

    //Method to go to next track
    private void goToTrack() {
        View v = getView();
        ImageButton playButton = (ImageButton) v.findViewById(R.id.trackplayer_play_button);

        //If current track is playing, stop it.
        mService.stop();

        //Shows pause button
        playButton.setSelected(true);

        //Set url and play
        mService.setUrl(SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).preview_url);
        mService.play();

        //Update the whole view to the new track
        //Getting the handle to the imageView
        ImageView albumArtworkView = (ImageView) v.findViewById(R.id.trackplayer_image);
        //Get the album image for the song in the 0 position.
        Image imageOfAlbumArtwork = SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).album.images.get(0);

        //Get the Url of the image
        String imageUrl = imageOfAlbumArtwork.url;
        //Set picasso to use that Url
        Picasso.with(getActivity()).load(imageUrl).into(albumArtworkView);

        //Getting the handle to the artist name textView
        TextView artistNameView = (TextView) v.findViewById(R.id.trackplayer_artist_name);
        //Getting the handle to the artist name from the SpotifyStreamerApplication at selected position
        Artist topTenArtist = SpotifyStreamerApplication.topTenTrackArtist;
        String artistName = topTenArtist.name;

        //Set textView to the artistName
        artistNameView.setText(artistName);

        //Getting the handle to the album name textView
        TextView albumNameView = (TextView) v.findViewById(R.id.trackplayer_albumn_name);
        String albumName = SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).album.name;
        //Set the textView to the album name
        albumNameView.setText(albumName);

        //Getting the handle to the track name textView
        TextView trackNameView = (TextView) v.findViewById(R.id.trackplayer_track_name);
        String trackName = SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).name;
        trackNameView.setText(trackName);
    }

    //Method to check position so that previous and next buttons don't go out of bounds.
    private void checkPositonIsValid(View v) {
        ImageButton previousButton = (ImageButton) v.findViewById(R.id.trackplayer_previous_button);
        ImageButton nextButton = (ImageButton) v.findViewById(R.id.trackplayer_next_button);

        previousButton.setEnabled(true);
        previousButton.setSelected(true);
        nextButton.setEnabled(true);
        nextButton.setSelected(true);

        //If at 0 position or array size is less or equal to 1, disable previous button
        if(SpotifyStreamerApplication.positionOfTrack == 0 || (SpotifyStreamerApplication.trackListHolder.tracks).size() <= 1){
            previousButton.setEnabled(false);
            previousButton.setSelected(false);
        }
        //If array size is less or equal to 1 or array size +1 is greater or equal to array size
        //disable next button
        if((SpotifyStreamerApplication.trackListHolder.tracks).size() <= 1 || ((SpotifyStreamerApplication.positionOfTrack+1 >= (SpotifyStreamerApplication.trackListHolder.tracks).size()))){
            nextButton.setEnabled(false);
            nextButton.setSelected(false);
        }
    }

    @Override
    public void onResume() {
        //Hard coded the dialog fragment to this width and height
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(getActivity(), MyService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
    }
}

