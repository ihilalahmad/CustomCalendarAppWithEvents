package com.example.semesterschedulingapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "semesterschedule.db";
    private static final String TABLE_NAME = "users";
    private static final String ID = "id";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static  final String USER_NAME = "username";
    private static final String EMAIL = "email";
    private static final  String PASSWORD = "password";

    private static final String EV_TABLE_NAME = "events";
    private static final String EV_TITLE = "evtitle";
    private static final String EV_DATE = "evda";
    private static final String EV_DESCRIPTION = "evdesc";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,firstname TEXT,lastname TEXT,username TEXT,email TEXT,password TEXT,evtitle TEXT,evda TEXT,evdesc TEXT)");

//        db.execSQL("CREATE TABLE " + EV_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,event_title TEXT,event_date TEXT,event_description TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

//        db.execSQL("DROP TABLE IF EXISTS " + EV_TABLE_NAME);

        onCreate(db);
    }

    public Boolean insertUser(String firstname, String lastname, String username, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIRSTNAME,firstname);
        values.put(LASTNAME,lastname);
        values.put(USER_NAME,username);
        values.put(EMAIL,email);
        values.put(PASSWORD, password);

        long result = db.insert(TABLE_NAME,null,values);

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor checkRepeatUser(String USER_NAME){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor search = null;

        try {
            search = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where username " + "='" + USER_NAME + "'", null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return search;
    }

    public Cursor getDetails(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_NAME ,null);
        return cursor;
    }

    public Boolean insertEvent(String event_title, String event_date, String event_description){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EV_TITLE,event_title);
        values.put(EV_DATE,event_date);
        values.put(EV_DESCRIPTION,event_description);

        long result = db.insert(TABLE_NAME,null,values);

        if (result == -1)
            return false;
        else
            return true;

    }

}
