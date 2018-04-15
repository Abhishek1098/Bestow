package com.bestow.hackmhs.bestow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

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

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


}
