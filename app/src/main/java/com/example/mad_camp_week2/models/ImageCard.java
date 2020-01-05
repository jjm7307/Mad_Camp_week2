package com.example.mad_camp_week2.models;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageCard {
    private  String Title;
    private String Description;
    private Bitmap bm;
    private Uri uri;

    public ImageCard(){

    }

    public ImageCard(String title, String description, Bitmap bitmap){
        Title = title;
        Description = description;
        bm = bitmap;
    }

    public ImageCard(String title, String description, Uri uri){
        Title = title;
        Description = description;
        this.uri = uri;
    }

    //Getter
    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public Bitmap getBitmap() {
        return bm;
    }

    public Uri getUri() {
        return uri;
    }

    //Setter

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setBitmap(Bitmap bitmap) {
        bm = bitmap;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
