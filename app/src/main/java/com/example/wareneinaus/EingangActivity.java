package com.example.wareneinaus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EingangActivity extends AppCompatActivity {

    Integer REQUEST_CODE_TAKE_PICTURE = 0xcaca;
    // This output image file uri is used by camera app to save taken picture.
    private Uri outputImgUri;
    // Save the camera taken picture in this folder.
    private File pictureSaveFolderPath;

    EditText editTextPhotoFiles;
    ArrayList<Uri> attachmentList =new ArrayList<Uri>();
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //only for init!
//        DBHelper dbHelper=new DBHelper(this);
//        dbHelper.doUpgrade();

        attachmentList.clear();

        String defEmail=utils.getSettingEmail(this);

        setContentView(R.layout.activity_eingang);

        EditText editTextDatum;
        editTextDatum=(EditText)findViewById(R.id.editTextDate);
        Date datumNow=new Date();

        editTextDatum.setText(utils.getDateString(datumNow));
        editTextDatum.setEnabled(false);

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

        EditText editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextEmail.setText(defEmail);

        editTextPhotoFiles=(EditText)findViewById(R.id.editTextFotos);

        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //https://medium.com/android-news/androids-new-image-capture-from-a-camera-using-file-provider-dd178519a954

        pictureSaveFolderPath = storageDir;

        Button btnAddPhoto=(Button)findViewById(R.id.btnAddPhoto);
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File outputImageFile = createImageFile();
                    // If cached temporary file exist then delete it.
                    if (outputImageFile.exists()) {
                        outputImageFile.delete();
                    }
                    // Get the output image file Uri wrapper object.
                    outputImgUri =
                            FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID+".provider", outputImageFile); //see Mainfest.xml for authority
                    if (outputImageFile != null) {
//                        context.grantUriPermission("", outputImgUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        // Startup camera app.
                        // Create an implicit intent which require take picture action..
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Specify the output image uri for the camera app to save taken picture.
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputImgUri);
                        // Start the camera activity with the request code and waiting for the app process result.
                        startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);
                        Log.d("TAG_TAKE_PICTURE", "Filename="+outputImgUri.getPath());
                    }
                }catch(IOException ex) {
                    Log.e("TAG_TAKE_PICTURE", ex.getMessage(), ex);
                }
                catch (IllegalArgumentException ex){
                    Log.e("TAG_TAKE_PICTURE", ex.getMessage(), ex);
                }
            }
        });
        Button btnSend=(Button) findViewById(R.id.btnSend);
        btnSend=(Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bCheckInput()){
                    return;
                }
                sendEmail(attachmentList);
            }
        });

        EditText editTextAbsender=(EditText)findViewById(R.id.editTextAbsender);
        Button btnOptionsAbsender=(Button)findViewById(R.id.btnAbsenderOptions);
        btnOptionsAbsender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions(editTextAbsender);
            }
        });
    }

    String mImageFilePath;
    File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mImageFilePath = "file:" + image.getAbsolutePath();
        Log.d("WARENEINGANG", "imagefile is"+mImageFilePath);
        return image;
    }

    Boolean bCheckInput(){
        Boolean b=false;
        int cnt=0;
        ArrayList<EditText> myEditTextList = new ArrayList<EditText>();
        myEditTextList.add((EditText)findViewById(R.id.editTextDate));
        myEditTextList.add((EditText)findViewById(R.id.editTextLieferrant));
        myEditTextList.add((EditText)findViewById(R.id.editTextArt));
        myEditTextList.add((EditText)findViewById(R.id.editTextAbsender));
        myEditTextList.add((EditText)findViewById(R.id.editTextName));
        myEditTextList.add((EditText)findViewById(R.id.editTextInhalt));
        myEditTextList.add((EditText)findViewById(R.id.editTextEmail));
        //myEditTextList.add((EditText)findViewById(R.id.editTextFotos));

        for(EditText edit: myEditTextList){
            String e=edit.getText().toString();
            if(e.length() == 0){
                edit.setBackgroundColor(Color.MAGENTA);
                cnt++;
            }else{
                edit.setBackgroundColor(Color.WHITE);
                b=true;
            }
        }
        if (cnt == myEditTextList.size()){
            cnt++;
        }
        return b;
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
                    attachmentList.add(outputImgUri);
                    updatePhotoList();
                    Log.d("PhotoTaken","saved to "+outputImgUri.toString());
                }
            }else if (requestCode == 1234){
                //email sent
                Toast.makeText(context, "EMail gesendet", Toast.LENGTH_LONG);
            }
        } catch (FileNotFoundException ex) {
            Log.e("TAG_TAKE_PICTURE", ex.getMessage(), ex);
        }
    }

    void updatePhotoList(){
        StringBuilder sb= new StringBuilder();
        for (Uri u: attachmentList){
            sb.append(u.getPath()+"\n");
        }
        editTextPhotoFiles.setText(sb.toString());
    }
    public void sendEmail(ArrayList<Uri> attachements) {
        try {
            String email = ((EditText)findViewById(R.id.editTextEmail)).getText().toString();
            String subject = "Waren-Eingag";

            String sDatum=((EditText)findViewById(R.id.editTextDate)).getText().toString();
            String sLieferrant=((EditText)findViewById(R.id.editTextLieferrant)).getText().toString();
            String sArt=((EditText)findViewById(R.id.editTextArt)).getText().toString();
            String sAbsender=((EditText)findViewById(R.id.editTextAbsender)).getText().toString();
            String sName =((EditText)findViewById(R.id.editTextName)).getText().toString();
            String sInhalt=((EditText)findViewById(R.id.editTextInhalt)).getText().toString();
            String message =
                    "Datum: " + sDatum+"\n"+
                    "Geliefert von: " + sLieferrant+ "\n"+
                    "Art: " + sArt + "\n" +
                    "Absender: " + sAbsender + "\n" +
                            "Name: " + sName + "\n" +
                    "Inhalt: " + sInhalt +"\n";
            //Do we need thos?
            ArrayList<CharSequence> aMessage=new ArrayList<CharSequence>();
            aMessage.add(message);
            final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);//as we attach multiple files
            //the below line was neccessary to avoid the root ClipData security exception
            emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachements);// attachements);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            //emailIntent.setData(Uri.parse("mailto:"));
//            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
            this.startActivityForResult(emailIntent, 1234);// Intent.createChooser(emailIntent, "Sending email..."));
            DBHelper db=new DBHelper(context);
            db.addWareneingang(sDatum,sLieferrant,sArt,sAbsender,sName,sInhalt,attachements.toString(), email);

            utils.setSettingEmail(this, email);

        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: "+ t.toString(), Toast.LENGTH_LONG).show();
            Log.d("WARENEINGANG", t.toString());
        }
    }

    public void showOptions(EditText editText)
    {
        String selected="";
        // setup the alert builder

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an Option");

        // add a list

        String[] options = {"Quad Computer", "Real", "Ingram", "Unbekannt"};

        //Pass the array list in Alert dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.setText(options[which]);
/*
                switch (which) {
                    case 0: editText.setText(options[0]);
                    case 1: // Config it as you need here
                    case 2:
                    case 3:
                    case 4:
                }
*/
            }
        });
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}