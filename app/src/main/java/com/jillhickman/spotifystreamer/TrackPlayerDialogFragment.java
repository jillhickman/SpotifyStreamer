package com.jillhickman.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by jillhickman on 7/27/15.
 */
public class TrackPlayerDialogFragment extends DialogFragment implements MediaPlayer.OnPreparedListener {

    public static final String TAG = "TrackPlayerDialogFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Gets rid of the title in the layout for this dialog fragment.
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.trackplayerlayout, container, false);

        prepareToPlay();

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

        //Getting the handle to the play button
        final ImageButton playButton = (ImageButton) v.findViewById(R.id.trackplayer_play_button);
        playButton.setEnabled(false);
        playButton.setOnClickListener(new View.OnClickListener() {

            //Call prepareToPlay so that music can be streamed in the background thread.
            @Override
            public void onClick(View v) {
                if(playButton.isSelected()== true){
                    SpotifyStreamerApplication.MyMediaPlayer.pause();
                    playButton.setSelected(false);
                }else{
                    SpotifyStreamerApplication.MyMediaPlayer.start();
                    playButton.setSelected(true);
                }
            }
        });

        //Implement pause button in layout
        //Getting the handle to the pause button


        //Getting the handle to the previous button
        ImageButton previousButton = (ImageButton) v.findViewById(R.id.trackplayer_previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position and decrement to previous position.
                SpotifyStreamerApplication.positionOfTrack--;
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
                goToTrack();
            }
        });

        //Getting the handle to the seek bar

        return v;
    }

    //Preparing the track so that it can be ready to play. Called when user pushes play.
    private void prepareToPlay() {
        //Get the track url for the song.
        String songUrl = SpotifyStreamerApplication.trackListHolder.tracks.get(SpotifyStreamerApplication.positionOfTrack).preview_url;

        //Streaming with Media player, got code from Media Playback API Guide
        //http://developer.android.com/guide/topics/media/mediaplayer.html#mediaplayer
        String url = songUrl; // your URL here
        SpotifyStreamerApplication.MyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            SpotifyStreamerApplication.MyMediaPlayer.reset();
            SpotifyStreamerApplication.MyMediaPlayer.setDataSource(url);
            //Set a listener to know when prepareAsync is done
            SpotifyStreamerApplication.MyMediaPlayer.setOnPreparedListener(this);
            //Calling prepareAsync so it can do the work on a background thread
            SpotifyStreamerApplication.MyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to go to next track
    private void goToTrack() {
        View v = getView();
        ImageButton playButton = (ImageButton) v.findViewById(R.id.trackplayer_play_button);

        //If current track is playing, stop it.
        if (SpotifyStreamerApplication.MyMediaPlayer.isPlaying()) {
            SpotifyStreamerApplication.MyMediaPlayer.stop();
        }

        //disabled play button is shown
        playButton.setEnabled(false);
        //It is a selected state of disable play button until the music is ready.
        playButton.setSelected(false);
        prepareToPlay();

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
    private void checkPositon() {
        if (SpotifyStreamerApplication.positionOfTrack == 0) {
            View v = getView();
            ImageButton previousButton = (ImageButton) v.findViewById(R.id.trackplayer_previous_button);
//            previousButton.
        }
    }

    public void pausePlay() {
        //Get the media player, tell is to pause, set that play button to not be selected
        SpotifyStreamerApplication.MyMediaPlayer.pause();
        ImageButton playButton = (ImageButton) getView().findViewById(R.id.trackplayer_play_button);
        playButton.setSelected(false);
    }

    @Override
    public void onResume() {
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

    //When prepareAsync is done, call this method so that the playButton can play music
    @Override
    public void onPrepared(MediaPlayer mp) {
//        mp.start();
        ImageButton playButton = (ImageButton) getView().findViewById(R.id.trackplayer_play_button);
        playButton.setEnabled(true);
//        playButton.setSelected(true);

    }
}

