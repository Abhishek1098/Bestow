package com.bestow.hackmhs.bestow;

import android.graphics.Bitmap;

/**
 * Created by Shiven Kumar on 4/14/2018.
 */

public class Item {

    private String username;
    private String description;
    private Bitmap image;
    private String town;

    public Item(String user, String desc, String city, Bitmap img){
        username=user;
        description=desc;
        town = city;
        image=img;
    }


    public String getUsername() {
        return username;
    }
    public Bitmap getImage() {
        return image;
    }
    public String getDescription() {
        return description;
    }
    public String getTown() {
        return town;
    }
    public void setTown(String town) {
        this.town = town;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
}
