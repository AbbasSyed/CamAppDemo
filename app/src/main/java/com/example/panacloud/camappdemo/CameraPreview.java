package com.example.panacloud.camappdemo;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by panacloud on 11/16/14.
 */

public class CameraPreview extends SurfaceView
        implements SurfaceHolder.Callback { //An interface that is used for callbacks to control the surface view

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) { //Constructor for the Surface View
        super(context);
        mCamera = camera;

        mHolder = getHolder(); //returns a Surface Holder object for controlling the Surface view
        mHolder.addCallback(this); //registers the Surface Holder object to receive callbacks

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) { //surface holder callback method
        try {
            mCamera.setPreviewDisplay(mHolder); //The holder provides a surface view to the Camera to show live previews
            mCamera.startPreview(); //starts live previews from the camera
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { //surface holder callback method

        if (mHolder.getSurface() == null) {

            return;
        }

        try {
            mCamera.stopPreview(); //Stops live previews from the camera
        } catch (Exception e) {

        }








        // implement code for rotate, picture resize, or
        // camera parameter changes here









        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { //surface holder callback method

    }
}