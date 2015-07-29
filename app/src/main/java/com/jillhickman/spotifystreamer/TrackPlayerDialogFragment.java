package com.jillhickman.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by jillhickman on 7/27/15.
 */
public class TrackPlayerDialogFragment extends DialogFragment{


    public static final String TAG = "TrackPlayerDialogFragment";

    //Making it a member variable because this holds a songTrack data to display in UI
    public Track mSongTrack;

    //Make it a member variable to manipulate the player.
     public MediaPlayer mMediaPlayer = new MediaPlayer();


//    static TrackPlayerDialogFragment newInstance() {
//        TrackPlayerDialogFragment f = new TrackPlayerDialogFragment();
//        return f;
//    }

    //Inflate the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.trackplayerlayout, container, false);

        Bundle mArgs = getArguments();
        int position = mArgs.getInt("song");

        //Get handle to the Tracks list
        Tracks allTracks = DataRepo.trackListHolder;
        //Getting the handle for the Track obj in the appropriate position
        mSongTrack= allTracks.tracks.get(position);

        //Getting the handle to the imageView
        ImageView albumArtworkView = (ImageView) v.findViewById(R.id.trackplayer_image);
        //Get the album image for the song in the 0 position.
        Image imageOfAlbumArtwork = mSongTrack.album.images.get(0);

        //Get the Url of the image
        String imageUrl = imageOfAlbumArtwork.url;
        //Set picasso to use that Url
        Picasso.with(getActivity()).load(imageUrl).into(albumArtworkView);

        //Getting the handle to the play button
        Button playButton = (Button) v.findViewById(R.id.trackplayer_play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the track url for the song.
                String songUrl = mSongTrack.preview_url;

                //Streaming with Media player, got code from Media Playback API Guide
                //http://developer.android.com/guide/topics/media/mediaplayer.html#mediaplayer
                String url = songUrl; // your URL here
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.prepare(); // might take long! (for buffering, etc)
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

}

