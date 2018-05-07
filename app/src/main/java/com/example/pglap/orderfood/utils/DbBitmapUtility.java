package com.example.pglap.orderfood.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DbBitmapUtility {

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
