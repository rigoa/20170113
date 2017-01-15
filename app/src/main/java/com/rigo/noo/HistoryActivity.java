package com.rigo.noo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.rigo.noo.db.NormalItemDBManager;
import com.rigo.noo.item.ItemArrayAdapter;
import com.rigo.noo.item.NormalItem;


import java.util.ArrayList;

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

        makeItemList();


    }

    public void makeItemList()
    {
        //make list sample
        ArrayList<NormalItem> nList = new ArrayList<NormalItem>();
        NormalItemDBManager.getInstance(getApplicationContext()).getAllNormalItem(nList);
        for(int i =1; i < 25; i++) {
            nList.add(new NormalItem("2016-01-0" + i, ApplicationDefine.STEP_METER, 15000));
        }
        mItemArrayAdapter = new ItemArrayAdapter(this, R.layout.item_noraml, nList);
        mListView.setAdapter(mItemArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeItemList();
        mItemArrayAdapter.notifyDataSetChanged();;
    }
}
