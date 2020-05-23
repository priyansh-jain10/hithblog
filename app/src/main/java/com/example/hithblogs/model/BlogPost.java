package com.example.hithblogs.model;

import java.sql.Timestamp;


public class BlogPost {


    public String user_id,imageurl,description,image_thumbnail;
    //public Timestamp timestamp;
    public BlogPost() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_thumbnail() {
        return image_thumbnail;
    }

    public void setImage_thumbnail(String image_thumbnail) {
        this.image_thumbnail = image_thumbnail;
    }

//    public Timestamp getTimestamp() {
//        return timestamp;
//    }

//    public void setTimestamp(Timestamp timestamp) {
//        this.timestamp = timestamp;
//    }

    public BlogPost(String user_id, String imageurl, String description, String image_thumbnail, Timestamp timestamp) {
        this.user_id = user_id;
        this.imageurl = imageurl;
        this.description = description;
        this.image_thumbnail = image_thumbnail;
        //this.timestamp = timestamp;
    }
}
