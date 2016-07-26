package com.geekmenship.app.util;


import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class mImageCropper {

    private String pictureURI;
    private Intent cropIntent = null;
    public boolean exceptionExists;

    public mImageCropper(String pictureURI) {
        this.pictureURI = pictureURI;
    }

    // The image crop method
    public Intent crop() {
        try {

            // Start Crop Activity
            cropIntent = new Intent("com.android.camera.action.CROP");

            // Indicate image type and Uri
            File file = new File(this.pictureURI);
            Uri contentUri = Uri.fromFile(file);

            // Sets the data and type
            cropIntent.setDataAndType(contentUri, "image/*");
            // Set crop properties
            cropIntent.putExtra("crop", "true");
            // Indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // Indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);
            // Retrieve data on return
            cropIntent.putExtra("return-data", true);

            // Indicates that there is no error
            this.exceptionExists = false;

            // Return an intent
            return cropIntent;

        } catch (UnsupportedOperationException e) {

            // Respond to users whose devices do not support the crop action
            cropIntent = new Intent();
            // Indicates that there is no error
            this.exceptionExists = true;

            // Return an intent
            return cropIntent;

        }
    }
}