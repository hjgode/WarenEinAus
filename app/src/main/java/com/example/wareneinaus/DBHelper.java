package com.example.wareneinaus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "wareneinaus.sqlite";
    public static final String WARENEINGANGTABLE_NAME = "wareneingang";

    public static final String WARENEINGANGCOLUMN_ID = "id";
    public static final String WARENEINGANGCOLUMN_DATUM = "datum";
    public static final String WARENEINGANGCOLUMN_LIEFERRANT = "lieferrant";
    public static final String WARENEINGANGCOLUMN_ART = "art";
    public static final String WARENEINGANGCOLUMN_ABSENDER = "absender";
    public static final String WARENEINGANGCOLUMN_INHALT = "inhalt";
    public static final String WARENEINGANGCOLUMN_FOTOS = "fotos";
    public static final String WARENEINGANGCOLUMN_EMAIL = "email";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 3);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+ WARENEINGANGTABLE_NAME +
                        "("+
                        WARENEINGANGCOLUMN_ID + " integer primary key autoincrement, " +
                        WARENEINGANGCOLUMN_DATUM + " text," +
                        WARENEINGANGCOLUMN_LIEFERRANT+" text,"+
                        WARENEINGANGCOLUMN_ART+" text,"+
                        WARENEINGANGCOLUMN_ABSENDER+" text," +
                        WARENEINGANGCOLUMN_INHALT+" text, "+
                        WARENEINGANGCOLUMN_FOTOS+" text," +
                        WARENEINGANGCOLUMN_EMAIL+" text"+
                        ")"
                );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+WARENEINGANGTABLE_NAME);
        onCreate(db);
    }
    public boolean addWareneingang(String datum,String lieferrant,String art,String absender, String inhalt, String fotos, String email){
        /*,*/
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WARENEINGANGCOLUMN_DATUM,datum);
        contentValues.put(WARENEINGANGCOLUMN_LIEFERRANT, lieferrant);
        contentValues.put(WARENEINGANGCOLUMN_ART,art);
        contentValues.put(WARENEINGANGCOLUMN_ABSENDER,absender);
        contentValues.put(WARENEINGANGCOLUMN_INHALT,inhalt);
        contentValues.put(WARENEINGANGCOLUMN_FOTOS,fotos);
        contentValues.put(WARENEINGANGCOLUMN_EMAIL,email);
        db.insert(WARENEINGANGTABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public ArrayList<String> getAllWareneingang(){
        ArrayList<String> arraylist= new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * from "+WARENEINGANGTABLE_NAME,null);

        if (cursor.moveToFirst()) {
            do {
                int i=cursor.getColumnIndex(WARENEINGANGCOLUMN_DATUM);
                arraylist.add(cursor.getString(i));
            } while (cursor.moveToNext());
        }
        return arraylist;
    }

//    public boolean updateStudentContact(Integer contactid,String contactname,String contactphone,String contactstreet,String contactemail, String contactplace)
//    {
//        /*,String contactname,*/
//        SQLiteDatabase db=this.getWritableDatabase();
//        ContentValues contantValues = new ContentValues();
//        contantValues.put("name",contactname);
//        contantValues.put("phone", contactphone);
//        contantValues.put("street",contactstreet);
//        contantValues.put("email",contactemail);
//        contantValues.put("place",contactplace);
//        db.update("mycontacts", contantValues, "id = ?", new String[]{Integer.toString(contactid)});
//        db.close();
//        return true;
//    }

}
