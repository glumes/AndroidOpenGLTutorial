package com.glumes.camera;

import android.media.ImageReader;

import com.orhanobut.logger.Logger;

/**
 * Created by glumes on 27/03/2018
 */

public class OnImageAvailableListener implements ImageReader.OnImageAvailableListener {


    @Override
    public void onImageAvailable(ImageReader reader) {
        Logger.d("onImageAvailable");
    }
}
