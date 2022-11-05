package com.example.wareneinaus;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.*;

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

    public static String getSettingEmail(Activity activity){
        SharedPreferences sharedPreferences = activity.getPreferences(MODE_PRIVATE);
        String defaultValue = "";
        String defEmail = sharedPreferences.getString("key_email", defaultValue);
        return defEmail;
    }
    public static void setSettingEmail(Activity activity, String semail){
        SharedPreferences sharedPreferences=activity.getPreferences(MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_email", semail);
        editor.apply();

    }
    /*
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_email", "email");
        editor.apply();

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);
        int highScore = sharedPref.getInt(getString(R.string.saved_high_score_key), defaultValue);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "";
        String defEmail = sharedPreferences.getString("key_email", defaultValue);

        utils.getSettingEmail(this);
*/

}
