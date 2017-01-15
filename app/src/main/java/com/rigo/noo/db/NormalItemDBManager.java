package com.rigo.noo.db;

import android.content.Context;

import com.rigo.noo.item.NormalItem;
import com.rigo.noo.util.AppLog;

import java.util.ArrayList;

/**
 * Created by kbg82 on 2017-01-14.
 */

public class NormalItemDBManager {
    private static String TAG = NormalItemDBManager.class.getSimpleName();

    private volatile static NormalItemDBManager mNormalItemDBManager	= null;
    private NormalItemDBAPI mNormalItemDBAPI = null;

    public static NormalItemDBManager getInstance(Context context) {
        if (mNormalItemDBManager == null) {
            synchronized (NormalItemDBManager.class) {
                if (mNormalItemDBManager == null) {
                    mNormalItemDBManager = new NormalItemDBManager(context);
                }
            }
        }
        return mNormalItemDBManager;
    }

    private NormalItemDBManager(Context context) {
        mNormalItemDBAPI = new NormalItemDBAPI(context);
    }

    public boolean isEmpty() {
        return mNormalItemDBAPI.isEmpty();
    }

    public void addNormalItemToDB(NormalItem aNormalItem) {
        mNormalItemDBAPI.addItem(aNormalItem);
    }

    public void getAllNormalItem( ArrayList<NormalItem> aListItem)
    {
        AppLog.i(TAG, "makeItemList");
        mNormalItemDBAPI.getAllItem(aListItem);
    }

    public void addRunItemToDB(NormalItem aNormalItem) {
        mNormalItemDBAPI.addRunItem(aNormalItem);
    }

    public NormalItem getRunItemFromDB()
    {
        return mNormalItemDBAPI.getRunItem();
    }
}
