package com.example.wareneinaus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class utils {

    public static String getDateString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:MM");
        //Date date = new Date();
        return sdf.format(date);
    }
}
