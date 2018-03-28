package com.glumes.camera;

/**
 * Created by glumes on 27/03/2018
 */

public interface ICamera {


    void openCamera();

    void closeCamera();


    void stopPreview();

    void setFacing(int facing);

    boolean isCameraOpened();

    int getFacing();

}
