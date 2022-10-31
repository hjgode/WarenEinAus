package com.example.wareneinaus;

import android.hardware.Camera;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class ImageCaptureCallback implements android.hardware.Camera.PictureCallback {

    private OutputStream filoutputStream;
    private String filename="";

    public ImageCaptureCallback(OutputStream filoutputStream) {
        this.filoutputStream = filoutputStream;
    }
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        try {
            Log.v(getClass().getSimpleName(), "onPictureTaken=" + data + " length = " + data.length);
            filename=utils.getTimestampPhotoName(new Date());
            FileOutputStream buf = new FileOutputStream("/sdcard/dcim/Camera/" + filename);
            buf.write(data);
            buf.flush();
            buf.close();
            // filoutputStream.write(data);
            filoutputStream.flush();
            filoutputStream.close();
        } catch (Exception ex) {
            filename="";
            ex.printStackTrace();
        }
    }

    public String getFilename(){
        return this.filename;
    }
}