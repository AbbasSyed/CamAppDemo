package com.example.panacloud.camappdemo;

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by panacloud on 11/16/14.
 */

public class MainActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout frameLayout;
    private Button mCaptureButton,mEffectButton;
    File mediaFile;
    int effectVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        mCaptureButton = (Button) findViewById(R.id.button1);
        mEffectButton = (Button) findViewById(R.id.button2);

//        mCamera = getCameraInstance();
        //Opens the Camera, Camera.CameraInfo class provides a static constant for the front facing camera
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);


        final Camera.PictureCallback mPicture = new Camera.PictureCallback() { //Callback interface for taking pictures from the camera

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {  //here the byte array data contains the picture data captured

                //get External Storage Public Directory returns an Internal Storage path on the Android Device. DIRECTORY_PICTURES adds an additional path to the pictures folder
                //the second parameter is a string which is the actual Folder name created inside Directory Pictures
                File pictureFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"CamAppDemo");
                pictureFile.mkdirs(); //creates a directory named by pictureFile.

                if (pictureFile == null){ //in case of unsuccessful creation of the pictureFile object return from the method.
                    Log.d("TAG", "Error creating media file, check storage permissions: ");
                    return;
                }

                mediaFile = new File(pictureFile,"myPic.jpeg"); //creates a new file inside the Directory Path of Picture File

                try {
                    FileOutputStream fos = new FileOutputStream(mediaFile); //writes the image data to this file.
                    fos.write(data);
                    fos.close();
                }  catch (IOException e) {
                    Log.d("TAG", "Error accessing file: " + e.getMessage());
                }

                //runs the media scanner service for refreshing the internal Android File System. Its important to call this so
                //that the newly created Picture can show up in the Android Devices Gallery app.
                MediaScannerConnection.scanFile(MainActivity.this, new String[]{pictureFile.getAbsolutePath()}, null, null);
                mCamera.startPreview(); //Starts the Preview again after the Picture is taken.
            }
        };



        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "photo captured", Toast.LENGTH_SHORT).show();
                mCamera.takePicture(null,null,mPicture); //takes a picture with the help of the PictureCallback object

            }
        });



        mEffectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera.Parameters cam_parameters = mCamera.getParameters(); //gets the current Parameter settings of the Camera
                effectVal++;

                switch (effectVal){ //switch case to cycle through the Camera effects
                    case 1:
                        cam_parameters.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE); //Sets live Camera Effects
                        Toast.makeText(MainActivity.this,"EFFECT NEGATIVE",Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        cam_parameters.setColorEffect(Camera.Parameters.EFFECT_AQUA);
                        Toast.makeText(MainActivity.this,"EFFECT AQUA",Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        cam_parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
                        Toast.makeText(MainActivity.this,"EFFECT SEPIA",Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        cam_parameters.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
                        Toast.makeText(MainActivity.this,"EFFECT SOLARIZE",Toast.LENGTH_SHORT).show();
                        break;

                    case 5:
                        cam_parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
                        Toast.makeText(MainActivity.this,"Normal",Toast.LENGTH_SHORT).show();
                        break;
                }
                mCamera.setParameters(cam_parameters); //sets the new Camera Parameter settings
                if(effectVal>5) effectVal=0;
            }
        });
    }


    private Camera getCameraInstance(){
        Camera c = null;

        try {
            c = Camera.open();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreview = new CameraPreview(this, mCamera);
        frameLayout.addView(mPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

}

