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

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by jillhickman on 6/27/15.
 */
public class SpotifyArtistAdapter extends ArrayAdapter<Artist> {
    //Array adapter constructor
    public SpotifyArtistAdapter(Context context, List<Artist> objects) {
        //I need to understand why we don't use resource, (pasted from Udacity github)
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, objects);
    }

    //Need to override this method since this is a custom adapter, then set the views
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Getting the handle for the Artist obj from the ArrayAdapter at the appropriate position
        Artist artist = DataRepo.artists.get(position);

        // Adapters recycle views to AdapterViews.<Learning purpose, paste from Udacity github>
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_searchresult, parent, false);
        }

        //Getting the handle to the imageView
        ImageView artistThumbnailView = (ImageView) convertView.findViewById(R.id.list_item_image);

        //Setting the imageView with artistThumbnail
        //Any time I need to get context in arrayAdapter, can call getContext()
        //Get the List of obj images
        List imageObjects = artist.images;
        //if the imageObject is not empty
        if (!imageObjects.isEmpty()){

            //Get one of the images
            //Grabbing the smallest image for the thumbnail, to use less data
            //If the image array size is greater than 1, get the image from the last position
            //else, get it from position 0.
            Image imageOfArtist;

            if (imageObjects.size() > 1) {
                  imageOfArtist = (Image) imageObjects.get((imageObjects.size() - 2));

            }else {
                imageOfArtist = (Image) imageObjects.get(0);
            }
            //Get the Url of the image
            String imageUrl = imageOfArtist.url;
            //Set picasso to use that Url
            Picasso.with(getContext()).load(imageUrl).into(artistThumbnailView);
        }

        //Getting the handle to the textView
        TextView artistNameView = (TextView) convertView.findViewById(R.id.list_item_text);

        //Get the name of artist
        String artistName = artist.name;
        //Set the artist textView
        artistNameView.setText(artistName);

        return convertView;
    }
}
