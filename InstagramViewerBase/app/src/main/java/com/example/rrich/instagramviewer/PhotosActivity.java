package com.example.rrich.instagramviewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import android.widget.ListView;


public class PhotosActivity extends Activity {
    public static final String CLIENT_ID = "701d5ff503444155b8eb2f6e61d12175";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>();
        // Create adapter and bind it to data in the Array List:
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // Populate the data into the list view:
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // Set the adapter to the listview (population of items):
        lvPhotos.setAdapter(aPhotos);
        // https://api.instagram.com/v1/media/popular?client_id=<client_id>
        // { "data" => [x] => "images" => "standard_resolution" => "url" }
        // Setup popular url endpoint:
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Create the network client:
        AsyncHttpClient client = new AsyncHttpClient();

        // Trigger the network request:
        // Handler parses response...
        client.get(popularUrl, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                // fired once the successful response back
                // response => popular photos JSON
                // url, height, username, caption
                // { "data" => [x] => "images" => "standard_resolution" => "url" }
                // { "data" => [x] => "images" => "standard_resolution" => "height" }
                // { "data" => [x] => "user" => "username" }
                // { "data" => [x] => "caption" => "text" }
                JSONArray photosJSON = null;
                try {
                    photos.clear(); // prevents errors from multiple calls to this method.
                    photosJSON = response.getJSONArray("data");
                    for (int i=0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        if (!photoJSON.isNull("caption")) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photos.add(photo);
                    }
                    // Notify the adapter that it should populate new changes into the list view:
                    aPhotos.notifyDataSetChanged(); // to minimize update calls to adapter
                } catch (JSONException e) {
                    // Fires if JSON parsing is invalid / fails
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        /*
        client.get(popularUrl, new JsonHttpResponseHandler(){
            // define the success and failure callbacks
            // Handle the successful response of the network request (i.e. the popular photos JSON):
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // fired once the successful response back
                // response => popular photos JSON
                // { "data" => [x] => "images" => "standard_resolution" => "url" }
                Log.i("INFO",response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
