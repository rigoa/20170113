package com.rigo.noo.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.rigo.noo.item.NormalItem;
import com.rigo.noo.util.AppLog;

import java.util.ArrayList;

/**
 * Created by kbg82 on 2017-01-14.
 */

public class NormalItemDBAPI {
    private static String TAG = NormalItemDBAPI.class.getSimpleName();

    public static final String AUTHORITY = "com.niro.noo";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private SQLiteDatabase mSQLiteDatabase = null;
    private Context mContext = null;
    private NormalItemDBHelper mNormalItemDBHelper = null;

    public NormalItemDBAPI(Context context) {
        mContext = context;
        mNormalItemDBHelper = new NormalItemDBHelper(context);
        mSQLiteDatabase = mNormalItemDBHelper.getReadableDatabase();
    }

    public boolean isEmpty() {
        Cursor cursor = null;
        boolean bResult = false;

        try{
            cursor = mSQLiteDatabase.rawQuery("SELECT * " + " FROM " + NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME, null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return  bResult;
    }

    public NormalItem getItem(String aStartTime) {
        Cursor cursor = null;
        NormalItem pResultItem = null;

        try{
            pResultItem = new NormalItem();

            cursor = mSQLiteDatabase.rawQuery(
                    "SELECT * FROM " + NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME
                            + " WHERE " + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME
                            + "=\"" + aStartTime + "\"", null);

            if(cursor.moveToFirst())
            {

                pResultItem.setStartTime( cursor.getString(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_STARTTIME));
                pResultItem.setDistance( cursor.getFloat(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_DISTANCE) );
                pResultItem.setData( cursor.getLong(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_DATA));
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return  pResultItem;

    }

    public void getAllItem(ArrayList<NormalItem> aListItem) {
        AppLog.i(TAG, "getAllItem");
        Cursor cursor = null;
        NormalItem ptItem = null;
        try{
            cursor = mSQLiteDatabase.rawQuery("SELECT * FROM "
                    + NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME, null);

            AppLog.i(TAG, "cursor.getCount() : " + cursor.getCount());

            while (cursor.moveToNext()){
                ptItem = new NormalItem();
                ptItem.setID( cursor.getInt(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_PKEY));
                ptItem.setStartTime( cursor.getString(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_STARTTIME));
                ptItem.setDistance( cursor.getFloat(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_DISTANCE) );
                ptItem.setData( cursor.getLong(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_DATA) );

                aListItem.add(ptItem);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
    }

    public void insertItem( NormalItem aNormalItem ) {

        ContentValues pContentValues = new ContentValues();

        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME, aNormalItem.getStartTime());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DISTANCE, aNormalItem.getDistance());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DATA, aNormalItem.getData());

        long nResult = mSQLiteDatabase.insert(NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME, null, pContentValues);
        if (nResult != -1) {
            //ContentResolver pContentResolver = mContext.getContentResolver();
            //pContentResolver.insert(CONTENT_URI, pContentValues);

        } else {
            //error process
        }
    }

    public void updateItem(NormalItem aNormalItem ) {

        ContentValues pContentValues = new ContentValues();

        NormalItem oldItem = getItem(aNormalItem.getStartTime());

        aNormalItem.setData(oldItem.getData() + aNormalItem.getData());

        String pWhere = "";

        pWhere = NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME + "=\""
                + aNormalItem.getStartTime() + "\"";

        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME, aNormalItem.getStartTime());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DISTANCE, aNormalItem.getDistance());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DATA, aNormalItem.getData());

        int nResult = mSQLiteDatabase.update(
                NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME, pContentValues, pWhere, null);

        if (nResult != -1) {
            //ContentResolver pContentResolver = mContext.getContentResolver();
            //pContentResolver.update(CONTENT_URI, pContentValues, pWhere, null);

        } else {
            //error process

        }

    }


    public boolean isDuplicate(NormalItem aNormalItem) {
        Cursor cursor = null;
        boolean result = false;

        try {
            cursor = mSQLiteDatabase.rawQuery(
                    "SELECT * FROM " + NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME
                            + " WHERE " + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME
                            + "=\"" + aNormalItem.getStartTime() + "\"", null);

            if (cursor.moveToFirst()) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public void addItem(NormalItem aNormalItem)
    {
        if(isDuplicate(aNormalItem))
        {
            updateItem(aNormalItem);
        }
        else
        {
            insertItem(aNormalItem);
        }

    }

    public NormalItem getRunItem() {
        Cursor cursor = null;
        NormalItem ptItem = null;
        try{
            cursor = mSQLiteDatabase.rawQuery("SELECT * FROM "
                    + NormalItemDBInfo.ITEM_RUN_DB_TABLE_NAME, null);

            ptItem = new NormalItem();
            if(cursor.moveToFirst()){
                ptItem.setStartTime( cursor.getString(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_STARTTIME));
                ptItem.setDistance( cursor.getFloat(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_DISTANCE) );
                ptItem.setData( cursor.getLong(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_INDEX_DATA) );
            }
            AppLog.i(TAG, "getRunItem data: " + ptItem.getData());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return  ptItem;
    }


    public void deleteItem(NormalItem aNormalItem)
    {
        String pWhere = NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_PEKY + "=\""
                + aNormalItem.getID() + "\"" + " AND "
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME + "=\""
                + aNormalItem.getStartTime() + "\"";

        try{
            int result = mSQLiteDatabase.delete(NormalItemDBInfo.ITEM_NORMAL_DB_TABLE_NAME, pWhere, null);

            if (result != -1) {
                ContentResolver pContentResolver = mContext.getContentResolver();
                pContentResolver.delete(CONTENT_URI, pWhere, null);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{

        }

    }

    public void insertRunItem( NormalItem aNormalItem ) {

        AppLog.i(TAG, "insertRunItem step" + aNormalItem.getData());

        ContentValues pContentValues = new ContentValues();

        long nFlag = 1;

        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME, aNormalItem.getStartTime());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DISTANCE, aNormalItem.getDistance());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DATA, aNormalItem.getData());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_FLAG, nFlag);

        long nResult = mSQLiteDatabase.insert(NormalItemDBInfo.ITEM_RUN_DB_TABLE_NAME, null, pContentValues);
        if (nResult != -1) {
            //ContentResolver pContentResolver = mContext.getContentResolver();
            //pContentResolver.insert(CONTENT_URI, pContentValues);

        } else {
            //error process
        }
    }

    public void updateRunItem(NormalItem aNormalItem ) {

        AppLog.i(TAG, "updateRunItem step " + aNormalItem.getData());

        ContentValues pContentValues = new ContentValues();

        String pWhere = "";

        long nFlag = 1;

        pWhere = NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_FLAG + "=\""
                + nFlag + "\"";

        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME, aNormalItem.getStartTime());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DISTANCE, aNormalItem.getDistance());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_DATA, aNormalItem.getData());
        pContentValues.put(NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_FLAG, nFlag);

        int nResult = mSQLiteDatabase.update(
                NormalItemDBInfo.ITEM_RUN_DB_TABLE_NAME, pContentValues, pWhere, null);

        if (nResult != -1) {
            //ContentResolver pContentResolver = mContext.getContentResolver();
            //pContentResolver.update(CONTENT_URI, pContentValues, pWhere, null);

        } else {
            //error process

        }

    }

    public void addRunItem(NormalItem aNormalItem)
    {
        Cursor cursor = null;

        try{
            cursor = mSQLiteDatabase.rawQuery("SELECT * FROM "
                    + NormalItemDBInfo.ITEM_RUN_DB_TABLE_NAME, null);

            if (cursor.moveToNext()) {
                updateRunItem(aNormalItem);
            }
            else {
                insertRunItem(aNormalItem);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
    }

    public void deleteRunItem(NormalItem aNormalItem)
    {
        String pWhere = NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_PEKY + "=\""
                + aNormalItem.getID() + "\"" + " AND "
                + NormalItemDBInfo.ITEM_NORMAL_DB_FIELD_STARTTIME + "=\""
                + aNormalItem.getStartTime() + "\"";

        try{
            int result = mSQLiteDatabase.delete(NormalItemDBInfo.ITEM_RUN_DB_TABLE_NAME, pWhere, null);

            if (result != -1) {
                ContentResolver pContentResolver = mContext.getContentResolver();
                pContentResolver.delete(CONTENT_URI, pWhere, null);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{

        }

    }

}
