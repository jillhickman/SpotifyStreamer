package com.jillhickman.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.jillhickman.spotifystreamer.Events.ChangedSeekBarEvent;
import com.jillhickman.spotifystreamer.Events.ChangedTrackTimeEvent;
import com.jillhickman.spotifystreamer.Events.DonePlayingEvent;
import com.jillhickman.spotifystreamer.Events.NowPlayingEvent;
import com.squareup.otto.Subscribe;

import java.io.IOException;

/**
 * Created by jillhickman on 8/3/15.
 */
public class MyService extends Service implements MediaPlayer.OnPreparedListener{

    public static String TAG = "MyService";

    public static String mUrl;

    //Member variable that starts a new thread,and handler for music duration
    private Handler mDurationHandler = new Handler();
    private Runnable mUpdateSeekBarTime = new Runnable() {

        public void run() {

            long timeElapsed = mMyMediaPlayer.getCurrentPosition();
            ChangedTrackTimeEvent changedTrackTimeEvent = new ChangedTrackTimeEvent(timeElapsed);
            SpotifyStreamerApplication.ottoBus.post(changedTrackTimeEvent);
            //Repeat this at every 500milliseconds
            mDurationHandler.postDelayed(this, 500);
        }
    };

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    //MyMediaPlayer obj. Need so we can access in dialog fragment
    public static MediaPlayer mMyMediaPlayer = new MediaPlayer();

    public MyService() {
        super();

        //Register with the bus
        SpotifyStreamerApplication.ottoBus.register(this);

        mMyMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            //Track is done playing
            @Override
            public void onCompletion(MediaPlayer mp) {
                long maxDuration = mp.getDuration();
                SpotifyStreamerApplication.ottoBus.post(new DonePlayingEvent(maxDuration));
            }
        });
    }
    /** method for clients */

    public static String getUrl() {
        return mUrl;
    }


    //Setting the url for the track I want to play
    public void setUrl (String url){
        mUrl = url;
    }

    public void play() {
        //Streaming with Media player, got code from Media Playback API Guide
        //http://developer.android.com/guide/topics/media/mediaplayer.html#mediaplayer
        mMyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mMyMediaPlayer.reset();
            mMyMediaPlayer.setDataSource(mUrl);
            //Set a listener to know when prepareAsync is done
            mMyMediaPlayer.setOnPreparedListener(this);
            //Calling prepareAsync so it can do the work on a background thread
            mMyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        //Set a listener to know when prepareAsync is done
        mMyMediaPlayer.start();
    }

    public void stop() {
        //If current track is playing, stop it.
        if (mMyMediaPlayer.isPlaying()) {
            mMyMediaPlayer.stop();
        }
    }

    public void pause() {
        if (mMyMediaPlayer.isPlaying()) {
            mMyMediaPlayer.pause();
        }
    }

    public boolean isPlaying() {
        return mMyMediaPlayer.isPlaying();
    }


    //When prepareAsync is done, call this method so that the playButton can play music
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        //Post the event, the progress of track
        mDurationHandler.postDelayed(mUpdateSeekBarTime, 100);

        long maxDuration = mp.getDuration();
        SpotifyStreamerApplication.ottoBus.post(new NowPlayingEvent(maxDuration));

    }

    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("bound", "bound");
        return mBinder;
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "Yikes, I'm being destroyed");

        //Unregister with the bus
        SpotifyStreamerApplication.ottoBus.unregister(this);
        super.onDestroy();
    }

    //This method listens to any ChangedSeekBarEvents that get posted on the bus
    //Event comes from the fragment. Use it to find position in track.
    @SuppressWarnings("unused")
    @Subscribe
    public void onUserChangedSeekbar (ChangedSeekBarEvent event) {

        Log.i(TAG, String.valueOf(event.mChangedSeekBar));
    }
}
