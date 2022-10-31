package com.example.wareneinaus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class utils {

    public static String getDateString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        //Date date = new Date();
        return sdf.format(date);
    }

    public static String getTimestampPhotoName(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hhmm.jpg");
        //Date date = new Date();
        return sdf.format(date);
    }

    public static final String[] LieferrantenList = new String[] {
            "DPD", "DHL", "UPS", "Spedition", "Anderes"
    };

    public static final String[] LieferungArt = new String[] {
            "Paket", "Palette", "Anderes"
    };
}
