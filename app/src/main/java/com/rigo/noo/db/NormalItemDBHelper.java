package com.rigo.noo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kbg82 on 2017-01-14.
 */

public class NormalItemDBHelper extends SQLiteOpenHelper {

    private static String TAG = NormalItemDBHelper.class.getSimpleName();

    public static final String NORMALITEM_DB_NAME = "NormalItem.db";
    public static final int NORMALITEM_DB_VERSION = 1;

    public NormalItemDBHelper(Context context) {
        super(context, NORMALITEM_DB_NAME, null, NORMALITEM_DB_VERSION);
        // TODO Auto-generated constructor stub
    }

    public NormalItemDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL( "CREATE TABLE " + NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME + "("
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_PEKY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME + " TEXT,"
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DISTANCE + " FLOAT,"
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DATA + " LONG);");

        db.execSQL( "CREATE TABLE " + NormalItemDBInfo.ITEM_RUN_DB_TABLE_NAME + "("
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_PEKY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME + " TEXT,"
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DISTANCE + " FLOAT,"
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DATA + " LONG,"
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_FLAG + " LONG);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME);
    }
}
