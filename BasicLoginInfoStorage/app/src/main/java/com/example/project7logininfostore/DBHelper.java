package com.example.project7logininfostore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//DB Helper class using SQLite
public class DBHelper extends SQLiteOpenHelper {

    //Constructor
    public DBHelper(Context context) {
        super(context, "LoginDB", null, 1);
    }

    //Creating a Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE LoginInfo(Site text primary key, User text, Password text)");
    }

    //Changing/Upgrading a Table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists LoginDB");
        onCreate(db);
    }
}
