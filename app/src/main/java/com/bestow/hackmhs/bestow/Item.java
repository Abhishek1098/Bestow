package com.bestow.hackmhs.bestow;

import android.graphics.Bitmap;

/**
 * Created by Shiven Kumar on 4/14/2018.
 */

public class Item {

    private String username, description, city, bitmapString;
    private Bitmap bitmap;


    public Item(String username, String description, String city, Bitmap bitmap){
        this.username=username;
        this.description=description;
        this.city = city;
        this.bitmap=bitmap;
    }

    public Item(String username, String description, String city, String bitmapString){
        this.username=username;
        this.description=description;
        this.city = city;
        this.bitmapString = bitmapString;
    }


    public String getUsername() {
        return username;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public String getDescription() {
        return description;
    }
    public String getCity() {
        return city;
    }
}
