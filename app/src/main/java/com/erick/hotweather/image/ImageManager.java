package com.erick.hotweather.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageManager {

    public static void loadImage(Context context, String imagePath, ImageView view) {
        if (imagePath != null) {
            Glide.with(context).load(imagePath).into(view);
        }
    }

}
