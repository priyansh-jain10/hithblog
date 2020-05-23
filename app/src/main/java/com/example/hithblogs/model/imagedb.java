package com.example.hithblogs.model;

import java.sql.Timestamp;

public class imagedb {
    private String imageUrl;
    private String UserId;
    private Timestamp timeadded;
    private String Username;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public imagedb(String imageUrl, String userId, String username) {
        this.imageUrl = imageUrl;
        UserId = userId;
        Username = username;
    }

    public imagedb() {
    }
}
