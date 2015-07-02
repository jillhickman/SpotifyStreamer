package com.jillhickman.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by jillhickman on 7/2/15.
 */
public class SpotifyTracksAdapter extends ArrayAdapter<Track> {
    //
    public  SpotifyTracksAdapter(Context context, List<Track> objects){

        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Getting one track for one row for the listView from the ArrayAdapter.
        Track track = DataRepo.trackListHolder.tracks.get(position);

        //Adapter recycle views to the AdapterViews
        //New view so have to inflate the layout.
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trackresults, parent, false);
        }
        //Getting the handle to the trackTextView
        TextView trackTextView = (TextView) convertView.findViewById(R.id.list_item_tracktext);
        //Get the name of the track
        String trackName = track.name;
        //Set the track name
        trackTextView.setText(trackName);

        //Getting the handle to the albumTextView, get name of album, and setting album name.
        TextView albumTextView = (TextView) convertView.findViewById(R.id.list_item_albumtext);
        String albumName = track.album.name;
        albumTextView.setText(albumName);

        //Getting the handle to the imageView
        ImageView albumnArtThumbnail = (ImageView) convertView.findViewById(R.id.list_item_image);
        //Get handle to the Track objs
        Track trackObjects = track;
        //Get the list of obj images
        List imageObject = trackObjects.album.images;

        //If the imageObject is not empty,
        if (!imageObject.isEmpty()){
            //get one of the images
            Image imageOfAlbum = (Image)imageObject.get(0);
            //get url of image
            String imageUrl = imageOfAlbum.url;
            //Set picasso to use the Url
            Picasso.with(getContext()).load(imageUrl).into(albumnArtThumbnail);
        }

        return convertView;
    }
}
