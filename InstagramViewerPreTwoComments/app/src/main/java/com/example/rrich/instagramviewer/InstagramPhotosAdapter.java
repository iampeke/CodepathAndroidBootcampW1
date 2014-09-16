package com.example.rrich.instagramviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

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
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
        TextView tvLikeCount = (TextView) convertView.findViewById(R.id.tvLikeCount);
        TextView tvTimeSince = (TextView) convertView.findViewById(R.id.tvSince);
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
        ImageView imgProfile = (ImageView) convertView.findViewById(R.id.imgProfile);
        ImageView imgLocation = (ImageView) convertView.findViewById(R.id.imgLocation);

        // Populate the subviews with the correct data:
        if (photo.caption != null && photo.caption != ""){
            tvCaption.setText(Html.fromHtml("<font color=\"#206199\"><b>"+ photo.username +" </b></font>" + "<font color=\"#000000\">" + photo.caption + "</font>"));
        }
        tvUsername.setText(photo.username);
        if (photo.locationName != null && photo.locationName != "") {
            tvLocation.setText(Html.fromHtml("<font color=\"#206199\">" + photo.locationName + "</font>"));
            imgLocation.setVisibility(View.VISIBLE);
        } else {
            tvLocation.setText("");
            imgLocation.setVisibility(View.INVISIBLE);
        }
        // Time Stamp:
        String relativeTimeStamp = calcTimeSincePost(photo.createdTime);
        tvTimeSince.setText(relativeTimeStamp);

        tvLikeCount.setText(photo.likesCount+"");
        // Set the height before loading:
        imgPhoto.getLayoutParams().height = photo.imageHeight;
        imgProfile.getLayoutParams().height = 50;
        imgProfile.getLayoutParams().width = 50;
        // Reset the image from the recycled view
        imgPhoto.setImageResource(0);
        imgProfile.setImageResource(0);
        // Ask for the photo to be added to the image view based on the photo URL:
        // Send network request to URL, download image bytes, convert to bitmap image, resizing image (?), insert bitmap into the ImageView
        // ALL in the background, async.
        // Easier to leverage a 3rd party library -> PICASSO
        Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
        Picasso.with(getContext()).load(photo.profileImageUrl).into(imgProfile);

        // Return view for that item:
        return convertView;
    }

    public String calcTimeSincePost(long postEpochTime) {
        long postEpochTimeMilli = postEpochTime * 1000;
        long epochTime = System.currentTimeMillis();
        String relativeTime = DateUtils.getRelativeTimeSpanString(postEpochTimeMilli, epochTime, 0, DateUtils.FORMAT_ABBREV_ALL).toString();
        return relativeTime;
    }

}
