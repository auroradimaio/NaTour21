package com.example.natour21.Item;

public class ReviewItem {
    private float Rating;
    private String Description;

    public ReviewItem(float rating, String description){
        Rating=rating;
        Description=description;
    }

    public float getRating(){
        return Rating;
    }

    public String getDescription(){
        return Description;
    }
}
