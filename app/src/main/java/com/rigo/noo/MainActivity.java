package com.rigo.noo;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.rigo.noo.db.NormalItemDBManager;
import com.rigo.noo.item.NormalItem;
import com.rigo.noo.sensor.StepSensorManager;
import com.rigo.noo.service.MiniService;

public class MainActivity extends ActivityGroup {

    private static String TAG = MainActivity.class.getSimpleName();

    private TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (TabHost)findViewById(R.id.tabhost);
        mTabHost.setup(getLocalActivityManager());

        mTabHost.addTab( mTabHost.newTabSpec("A").setIndicator("A").setContent(new Intent(this, UserActivity.class)));
        mTabHost.addTab( mTabHost.newTabSpec("B").setIndicator("B").setContent(new Intent(this, HistoryActivity.class)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        StepSensorManager.getInstance(getApplicationContext()).UnsetSensorCallback(null);

        NormalItem pNormalItem;
        pNormalItem = NormalItemDBManager.getInstance(getApplicationContext()).getRunItemFromDB();
        if( pNormalItem != null && pNormalItem.getDistance() > 0 ) {
            startService(new Intent(this, MiniService.class));
        }
    }

    @Override
    protected void onResume() {
        stopService(new Intent(this, MiniService.class));
        super.onResume();

    }
}
