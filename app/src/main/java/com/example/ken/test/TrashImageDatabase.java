package com.example.ken.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TrashImageDatabase extends SQLiteOpenHelper{

    private static final String DBNAME = "TIDB";
    private static final int DBVERSION = 1;


    public TrashImageDatabase(Context context){
        super(context,DBNAME,null,DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table TIdb"+
                "("+
                "_id integer primary key autoincrement"+
                ",image_path text unique"+
                ",image_id integer"+
                ",score integer"+
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){

    }
}