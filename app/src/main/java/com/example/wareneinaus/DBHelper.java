package com.example.wareneinaus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "wareneinaus.sqlite";
    public static final String WARENEINGANG_TABLE_NAME = "wareneingang";

    public static final String WARENEINGANGCOLUMN_ID = "id";
    public static final String WARENEINGANGCOLUMN_DATUM = "datum";
    public static final String WARENEINGANGCOLUMN_LIEFERRANT = "lieferrant";
    public static final String WARENEINGANGCOLUMN_ART = "art";
    public static final String WARENEINGANGCOLUMN_ABSENDER = "absender";
    public static final String WARENEINGANGCOLUMN_NAME = "name";
    public static final String WARENEINGANGCOLUMN_INHALT = "inhalt";
    public static final String WARENEINGANGCOLUMN_FOTOS = "fotos";
    public static final String WARENEINGANGCOLUMN_EMAIL = "email";

    Context _context;
    ListAdapter adapter=null;

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
                        WARENEINGANGCOLUMN_NAME+" text," +
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
    public void doUpgrade(){
        SQLiteDatabase db=this.getWritableDatabase();
        this.onUpgrade(db,1,2);
    }
    public boolean addWareneingang(String datum,String lieferrant,String art,String absender, String name, String inhalt, String fotos, String email){
        /*,*/
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(WARENEINGANGCOLUMN_DATUM, datum);
            contentValues.put(WARENEINGANGCOLUMN_LIEFERRANT, lieferrant);
            contentValues.put(WARENEINGANGCOLUMN_ART, art);
            contentValues.put(WARENEINGANGCOLUMN_ABSENDER, absender);
            contentValues.put(WARENEINGANGCOLUMN_NAME, name);
            contentValues.put(WARENEINGANGCOLUMN_INHALT, inhalt);
            contentValues.put(WARENEINGANGCOLUMN_FOTOS, fotos);
            contentValues.put(WARENEINGANGCOLUMN_EMAIL, email);
            db.insert(WARENEINGANG_TABLE_NAME, null, contentValues);
            db.notifyAll();
            db.close();
        }catch (Exception ex){
            Log.d("DBHelper","addWareneingang: Exception "+ex.getMessage());
        }
        return true;
    }

    public void fillList2(Context context, ListView listView){
//        DBHelper db = this;
        ArrayList<HashMap<String, String>> dataList = this.GetAllWareneingang2();
        ListView lv = listView;// (ListView) findViewById(R.id.user_list);
        adapter = new SimpleAdapter(context, dataList, R.layout.eingang_details_templ,
                new String[]{"id", "datum", "absender", "inhalt"},
                new int[]{R.id.row_id, R.id.row_datum, R.id.row_absender, R.id.row_inhalt});

        lv.setAdapter(adapter);
    }

    // Get  Details
    public ArrayList<HashMap<String, String>> GetAllWareneingang2() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<HashMap<String, String>> wareneingangList = new ArrayList<>();
        String query = "SELECT "+
                WARENEINGANGCOLUMN_ID +","+
                WARENEINGANGCOLUMN_DATUM +","+
                WARENEINGANGCOLUMN_ABSENDER +","+
                WARENEINGANGCOLUMN_INHALT +" "+
                " FROM "+ WARENEINGANG_TABLE_NAME + " ORDER BY "+ WARENEINGANGCOLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> daten = new HashMap<>();
            int cx = cursor.getColumnIndex(WARENEINGANGCOLUMN_ID);
            daten.put("id",cursor.getString(cx));
            cx = cursor.getColumnIndex(WARENEINGANGCOLUMN_DATUM);
            daten.put("datum",cursor.getString(cx));
            cx=cursor.getColumnIndex(WARENEINGANGCOLUMN_ABSENDER);
            daten.put("absender",cursor.getString(cx));
            cx=cursor.getColumnIndex(WARENEINGANGCOLUMN_INHALT);
            daten.put("inhalt",cursor.getString(cx));
            wareneingangList.add(daten);
        }
        return  wareneingangList;
    }

}
