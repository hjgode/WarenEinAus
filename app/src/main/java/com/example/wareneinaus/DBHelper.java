package com.example.wareneinaus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "create table mycontacts " +
                        "(id integer primary key autoincrement, name text,phone text,email text, street text,place text)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS mycontacts");
        onCreate(db);
    }
    public boolean addWareneingang(String contactname,String contactphone,String contactstreet,String contactemail, String contactplace){
        /*,*/
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put("name",contactname);
        contantValues.put("phone", contactphone);
        contantValues.put("street",contactstreet);
        contantValues.put("email",contactemail);
        contantValues.put("place",contactplace);
        db.insert("mycontacts", null, contantValues);
        db.close();
        return true;
    }
    public boolean updateStudentContact(Integer contactid,String contactname,String contactphone,String contactstreet,String contactemail, String contactplace)
    {
        /*,String contactname,*/
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put("name",contactname);
        contantValues.put("phone", contactphone);
        contantValues.put("street",contactstreet);
        contantValues.put("email",contactemail);
        contantValues.put("place",contactplace);
        db.update("mycontacts", contantValues, "id = ?", new String[]{Integer.toString(contactid)});
        db.close();
        return true;
    }

}
