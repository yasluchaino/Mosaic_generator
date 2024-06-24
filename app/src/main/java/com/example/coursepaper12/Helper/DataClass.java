package com.example.coursepaper12.Helper;

public class DataClass {
    private String imageURL;
    public DataClass(){
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public DataClass(String imageURL) {
        this.imageURL = imageURL;
    }
}