package com.example.wareneinaus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EingangActivity extends AppCompatActivity {

    Integer REQUEST_CODE_TAKE_PICTURE = 0xcaca;
    // This output image file uri is used by camera app to save taken picture.
    private Uri outputImgUri;
    // Save the camera taken picture in this folder.
    private File pictureSaveFolderPath;

    EditText editTextPhotoFiles;
    ArrayList<Uri> attachemntList=new ArrayList<Uri>();
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attachemntList.clear();

        setContentView(R.layout.activity_eingang);

        EditText editTextDatum;
        editTextDatum=(EditText)findViewById(R.id.editTextDate);
        Date datumNow=new Date();

        editTextDatum.setText(utils.getDateString(datumNow));

        AutoCompleteTextView autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.editTextLieferrant);
        ArrayAdapter<String> adapterLieferrant = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, utils.LieferrantenList);
        autoCompleteTextView.setAdapter(adapterLieferrant);
        autoCompleteTextView.setThreshold(1);

        AutoCompleteTextView autoCompleteTextViewArt=(AutoCompleteTextView)findViewById(R.id.editTextArt);
        ArrayAdapter<String> adapterArt = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, utils.LieferungArt);
        autoCompleteTextViewArt.setAdapter(adapterArt);
        autoCompleteTextViewArt.setThreshold(1);

        editTextPhotoFiles=(EditText)findViewById(R.id.editTextFotos);

        // Get this app's external cache directory, manipulate this directory in app do not need android os system permission check.
        // The cache folder is application specific, when the app is uninstalled it will be removed also.
        pictureSaveFolderPath = getExternalCacheDir();

        Button btnAddPhoto=(Button)findViewById(R.id.btnAddPhoto);
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    // Create a random image file name.
                    String imageFileName = "outputImage_" + System.currentTimeMillis() + ".png";

                    // Construct a output file to save camera taken picture temporary.
                    File outputImageFile = new File(pictureSaveFolderPath, imageFileName);

                    // If cached temporary file exist then delete it.
                    if (outputImageFile.exists()) {
                        outputImageFile.delete();
                    }

                    // Create a new temporary file.
                    outputImageFile.createNewFile();

                    // Get the output image file Uri wrapper object.
                    outputImgUri = getImageFileUriByOsVersion(outputImageFile);

                    // Startup camera app.
                    // Create an implicit intent which require take picture action..
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Specify the output image uri for the camera app to save taken picture.
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputImgUri);

                    // Start the camera activity with the request code and waiting for the app process result.
                    startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);
                    Log.d("TAG_TAKE_PICTURE", "Filename="+outputImgUri.getPath());
                }catch(IOException ex) {
                    Log.e("TAG_TAKE_PICTURE", ex.getMessage(), ex);
                }
                catch (IllegalArgumentException ex){
                    Log.e("TAG_TAKE_PICTURE", ex.getMessage(), ex);
                }
            }
        });

    }
    /* Get the file Uri object by android os version.
     *  return a Uri object. */
    private Uri getImageFileUriByOsVersion(File file)
    {
        Uri ret = null;

        // Get output image unique resource identifier. This uri is used by camera app to save taken picture temporary.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            // /sdcard/ folder link to /storage/41B7-12F1 folder
            // so below code return /storage/41B7-12F1
            File externalStorageRootDir = Environment.getExternalStorageDirectory();

            // contextRootDir = /data/user/0/com.dev2qa.example/files in my Huawei mate 8.
            File contextRootDir = getFilesDir();

            // contextCacheDir = /data/user/0/com.dev2qa.example/cache in my Huawei mate 8.
            File contextCacheDir = getCacheDir();

            // For android os version bigger than or equal to 7.0 use FileProvider class.
            // Otherwise android os will throw FileUriExposedException.
            // Because the system considers it is unsafe to use local real path uri directly.
            Context ctx = getApplicationContext();
            ret = FileProvider.getUriForFile(ctx, "com.example.wareneinaus.fileprovider", file);
        }else
        {
            // For android os version less than 7.0 there are no safety issue,
            // So we can get the output image uri by file real local path directly.
            ret = Uri.fromFile(file);
        }

        return ret;
    }

    /* This method is used to process the result of camera app. It will be invoked after camera app return.
    It will show the camera taken picture in the image view component. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // Process result for camera activity.
            if (requestCode == REQUEST_CODE_TAKE_PICTURE) {

                // If camera take picture success.
                if (resultCode == RESULT_OK) {

                    // Get content resolver.
                    ContentResolver contentResolver = getContentResolver();

                    // Use the content resolver to open camera taken image input stream through image uri.
                    InputStream inputStream = contentResolver.openInputStream(outputImgUri);

                    // Decode the image input stream to a bitmap use BitmapFactory.
                    Bitmap pictureBitmap = BitmapFactory.decodeStream(inputStream);

                    // Set the camera taken image bitmap in the image view component to display.
                    //takePictureImageView.setImageBitmap(pictureBitmap);
                    //String sFilename=outputImgUri.getPath();
                    //editTextPhotoFiles.setText(editTextPhotoFiles.getText().append(sFilename)+";");
                    attachemntList.add(outputImgUri);
                    updatePhotoList();
                }
            }
        } catch (FileNotFoundException ex) {
            Log.e("TAG_TAKE_PICTURE", ex.getMessage(), ex);
        }
    }

    void updatePhotoList(){
        StringBuilder sb= new StringBuilder();
        for (Uri u:attachemntList){
            sb.append(u.getPath()+"\n");
        }
        editTextPhotoFiles.setText(sb.toString());
    }
    public void sendEmail(ArrayList<Uri> attachemnts) {
        try {
            String email = "";//etEmail.getText().toString();
            String subject = "";//etSubject.getText().toString();
            String message = "";//etMessage.getText().toString();
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachemnts);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: "+ t.toString(), Toast.LENGTH_LONG).show();
        }
    }

}