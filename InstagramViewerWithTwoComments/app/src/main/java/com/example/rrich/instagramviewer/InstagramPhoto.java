package com.example.rrich.instagramviewer;

/**
 * Created by rrich on 9/13/14.
 */
public class InstagramPhoto {
    public String username;
    public String caption;
    public String imageUrl;
    public String profileImageUrl;
    public int imageHeight;
    public int likesCount;
    public String locationName;
    public long createdTime;
    // Most recent Comment:
    public String commentUsername1;
    public String comment1;
    // Second most recent comment:
    public String commentUsername2;
    public String comment2;

    public String toString() {
        return "hello - " + imageUrl;
    }


}
