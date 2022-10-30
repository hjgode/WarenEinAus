package com.example.wareneinaus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EingangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eingang);

        EditText editTextDatum;
        editTextDatum=(EditText)findViewById(R.id.editTextDate);
        Date datumNow=new Date();

        editTextDatum.setText(utils.getDateString(datumNow));
    }
}