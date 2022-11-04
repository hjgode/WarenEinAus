package com.example.wareneinaus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.service.carrier.CarrierMessagingClientService;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Date;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    Context context=this;
    DBHelper dbHelper;

    final static int PERMISSION_REQUEST_CODE = 200;
    String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, PERMISSION_REQUEST_CODE);
        }

        Button btnWarenEingang;
        btnWarenEingang = (Button) findViewById(R.id.btnWarenEingang);
        btnWarenEingang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EingangActivity.class);
                startActivity(intent);
            }
        });
        ListView listViewEingang = (ListView) findViewById(R.id.listViewEingang);
        dbHelper = new DBHelper(context);
        String datum=utils.getDateString(new Date());
//        dbHelper.addWareneingang(datum,"DPD","Paket","Absender","TM T88V","","hjgode@gmail.com");
        dbHelper.fillList2(context, listViewEingang);
    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(permsRequestCode,permissions,grantResults);
        switch(permsRequestCode){
            case 200:
                boolean locationAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

}