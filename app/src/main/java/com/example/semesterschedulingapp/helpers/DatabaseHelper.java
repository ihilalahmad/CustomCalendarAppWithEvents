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
    private static final String TASKTITLE = "tasktitle";
    private static final String TASKDETAILS = "taskdetails";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,tasktitle TEXT,taskdetails TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertTask(String title, String details){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASKTITLE, title);
        values.put(TASKDETAILS, details);

        long result = db.insert(TABLE_NAME,null,values);

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getTaskDetails(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_NAME + " ORDER BY " + ID + " DESC",null);
        return cursor;
    }


}
