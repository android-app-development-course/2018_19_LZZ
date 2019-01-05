package com.example.jie95.fakeoffice.FileSelected;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    private static String DATABASE_NAME="Filetest1.db";
    private static int DATABASE_VERSION=1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_diary(_id integer primary key autoincrement,title vechar(70),path verchar(100),pubdate)");
        db.execSQL("create table tb_file(_id integer primary key autoincrement,title vechar(70),path verchar(100),pubdate)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}
