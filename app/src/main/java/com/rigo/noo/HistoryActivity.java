package com.rigo.noo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.rigo.noo.db.NormalItemDBManager;
import com.rigo.noo.item.ItemArrayAdapter;
import com.rigo.noo.item.NormalItem;
import com.rigo.noo.util.AppLog;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kbg82 on 2017-01-13.
 */

public class HistoryActivity extends Activity{
    private static String TAG = HistoryActivity.class.getSimpleName();

    private ListView mListView;
    private ItemArrayAdapter mItemArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mListView = (ListView)findViewById(R.id.lvItem);
    }

    public void makeItemList()
    {
        AppLog.i(TAG, "makeItemList");
        //make list sample
        ArrayList<NormalItem> nList = new ArrayList<NormalItem>();
        NormalItemDBManager.getInstance(this).getAllNormalItem(nList);
        //Test List View Code
//        for(int i =1; i < 5; i++) {
//            nList.add(new NormalItem("2016-01-0" + i, ApplicationDefine.STEP_METER, 15000));
//        }

        Collections.sort(nList);

        mItemArrayAdapter = new ItemArrayAdapter(this, R.layout.item_noraml, nList);
        mListView.setAdapter(mItemArrayAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppLog.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLog.i(TAG, "onResume");
        makeItemList();
        mItemArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLog.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLog.i(TAG, "onStop");
    }
}
