package com.rigo.noo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.rigo.noo.db.NormalItemDBHelper;
import com.rigo.noo.db.NormalItemDBInfo;
import com.rigo.noo.util.AppLog;

/**
 * Created by kbg82 on 2017-01-15.
 */

public class NooProvider extends ContentProvider {
    private static String TAG = NooProvider.class.getSimpleName();

    NormalItemDBHelper mNormalItemDBHelper = null;
    SQLiteDatabase mSQLiteDatabase = null;

    //static final Uri NOO_CONTENT_URI = Uri.parse("content://com.rigo.noo.provider");

    public NooProvider() {
        super();
    }

    @Override
    public boolean onCreate() {
        mNormalItemDBHelper = new NormalItemDBHelper(getContext());
        mSQLiteDatabase = mNormalItemDBHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        AppLog.i(TAG, "noo provider query");

        Cursor cursor = null;
        try {
            cursor = mSQLiteDatabase.query(NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME, projection,
                            selection, selectionArgs, null, null, sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppLog.i(TAG, "query result : " + cursor);
        return cursor;

        //return null;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }
}
