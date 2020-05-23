package com.example.hithblogs.util;

import android.app.Application;
import android.net.Uri;

public class imagemodel extends Application {
    public Uri imageUri;

    public imagemodel() {
    }

    public imagemodel(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
