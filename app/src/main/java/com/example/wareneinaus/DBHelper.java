package com.example.wareneinaus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "wareneinaus.sqlite";
    public static final String WARENEINGANG_TABLE_NAME = "wareneingang";

    public static final String WARENEINGANGCOLUMN_ID = "id";
    public static final String WARENEINGANGCOLUMN_DATUM = "datum";
    public static final String WARENEINGANGCOLUMN_LIEFERRANT = "lieferrant";
    public static final String WARENEINGANGCOLUMN_ART = "art";
    public static final String WARENEINGANGCOLUMN_ABSENDER = "absender";
    public static final String WARENEINGANGCOLUMN_INHALT = "inhalt";
    public static final String WARENEINGANGCOLUMN_FOTOS = "fotos";
    public static final String WARENEINGANGCOLUMN_EMAIL = "email";

    Context _context;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 3);
        _context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table IF NOT EXISTS "+ WARENEINGANG_TABLE_NAME+" "+
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
        db.execSQL("DROP TABLE IF EXISTS "+ WARENEINGANG_TABLE_NAME);
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
        db.insert(WARENEINGANG_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getAllWareneingang(){
        ArrayList<String> arraylist= new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
        try{
            cursor=db.rawQuery("Select * from "+ WARENEINGANG_TABLE_NAME,null);
            return cursor;

        }
        catch (Exception ex){
            Toast.makeText(_context, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return cursor;
    }

    public void FillList(Context context, ListView listView) {
        try {
            int[] id = {R.id.txtListElement};
            String[] CompanyName = new String[]{"CompanyName"};
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor c = this.getAllWareneingang();
            String[] names=new String[]{"datum"};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                    R.layout.list_eingang_templ, c, names, 0, 0);
            listView.setAdapter(adapter);

        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
            Log.d("DBHelper", ex.getMessage());
        }
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
