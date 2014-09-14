package com.example.rrich.instagramviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rrich on 9/13/14.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, ArrayList<InstagramPhoto> photos) {
        super(context, android.R.layout.simple_list_item_1, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Take the data source at position:
        // Get the data item:
        InstagramPhoto photo = getItem(position);

        // Check if we are using a recycled view:
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        // Look up the subviews within the template:
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);

        // Populate the subviews with the correct data:
        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        // Set the height before loading:
        imgPhoto.getLayoutParams().height = photo.imageHeight;
        // Reset the image from the recycled view
        imgPhoto.setImageResource(0);
        // Ask for the photo to be added to the image view based on the photo URL:
        // Send network request to URL, download image bytes, convert to bitmap image, resizing image (?), insert bitmap into the ImageView
        // ALL in the background, async.
        // Easier to leverage a 3rd party library -> PICASSO
        Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);

        // Return view for that item:
        return convertView;
    }

}
