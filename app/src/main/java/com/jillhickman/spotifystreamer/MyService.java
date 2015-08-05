package com.jillhickman.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by jillhickman on 8/3/15.
 */
public class MyService extends Service implements MediaPlayer.OnPreparedListener{

    public static String TAG = "MyService";
    public static String mUrl;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    //MyMediaPlayer obj. Need so we can access in dialog fragment
    public static MediaPlayer mMyMediaPlayer = new MediaPlayer();

    //Constructor
    public MyService() {
        super();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("This","that");
//        String url ="https://p.scdn.co/mp3-preview/e8c8720e4e8026413748263e9ce889aa9c58fe2f"; // your URL here
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            mediaPlayer.setDataSource(url);
//            mediaPlayer.prepare(); // might take long! (for buffering, etc)
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mediaPlayer.start();
//        return super.onStartCommand(intent, flags, startId);
//    }

    /** method for clients */
    public void playSong() {

        Log.d("This","that");
        String url ="https://p.scdn.co/mp3-preview/e8c8720e4e8026413748263e9ce889aa9c58fe2f"; // your URL here
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)

        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
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

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }


    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "Yikes, I'm being destroyed");
        super.onDestroy();
    }
}
