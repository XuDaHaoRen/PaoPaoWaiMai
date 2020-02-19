package com.xbl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HG on 2016/11/15.
 */
public class MyDBHelper extends SQLiteOpenHelper {
    String register_sql="create table user("
            +"id integer primary key autoincrement not null, "
            +"phone varchar(11) not null,"
            +"pass nvarchar not null,"
            +"nickname nvarchar ,"
            +"sex varchar(2) ,"
            +"address nvarchar "+
    ");";
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(register_sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
