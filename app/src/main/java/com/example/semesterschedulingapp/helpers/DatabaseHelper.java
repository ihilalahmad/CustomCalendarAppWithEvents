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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,firstname TEXT,lastname TEXT,username TEXT,email TEXT,password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertUser(String firstname, String lastname, String username, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("firstname",firstname);
        values.put("lastname",lastname);
        values.put("username",username);
        values.put("email",email);
        values.put("password", password);

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

    public Cursor checkRepeatEmail(String EMAIL){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor search = null;

        try {
            search = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where email " + "='" + EMAIL + "'", null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return search;
    }

    public Cursor getUser(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_NAME + " ORDER BY id DESC ",null);
        return cursor;
    }

    public Cursor getDetails(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_NAME ,null);
        return cursor;
    }

    public Integer deleteUser(String user){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("ListDeleted", " " + user);
        Integer i = db.delete(TABLE_NAME,"username = ? ", new String[]{user});

        return i;
    }
}
