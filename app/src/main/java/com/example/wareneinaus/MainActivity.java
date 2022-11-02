package com.example.wareneinaus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.carrier.CarrierMessagingClientService;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Context context=this;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        dbHelper.addWareneingang(datum,"DPD","Paket","Absender","TM T88V","","hjgode@gmail.com");
        dbHelper.FillList(context, listViewEingang);
    }
}