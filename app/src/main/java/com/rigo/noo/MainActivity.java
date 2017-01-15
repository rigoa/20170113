package com.rigo.noo;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TabHost;

import com.rigo.noo.db.NormalItemDBManager;
import com.rigo.noo.item.NormalItem;
import com.rigo.noo.sensor.StepSensorManager;
import com.rigo.noo.service.MiniService;
import com.rigo.noo.util.AppLog;


public class MainActivity extends ActivityGroup {

    private static String TAG = MainActivity.class.getSimpleName();

    private TabHost mTabHost;

    private final int ACCESS_FINE_LOCATION_INDEX = 0;
    private final int ACCESS_COARSE_LOCATION_INDEX = 1;

    private static int mIsPermission = ApplicationDefine.MAIN_PERMISSION_WAIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (TabHost)findViewById(R.id.tabhost);
        mTabHost.setup(getLocalActivityManager());

        mTabHost.addTab( mTabHost.newTabSpec("A").setIndicator(getResources().getString(R.string.activity_name_user)).setContent(new Intent(this, UserActivity.class)));
        mTabHost.addTab( mTabHost.newTabSpec("B").setIndicator(getResources().getString(R.string.activity_name_history)).setContent(new Intent(this, HistoryActivity.class)));

        mIsPermission = ApplicationDefine.MAIN_PERMISSION_WAIT;
    }

    public  void getLocationPermition() {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            AppLog.i(TAG, "onCreate requestPermissions");
            String [] pRequired= new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, pRequired, ApplicationDefine.PERMITION_RESULT_ID_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case ApplicationDefine.PERMITION_RESULT_ID_LOCATION: {
                if( grantResults[ACCESS_FINE_LOCATION_INDEX] ==  PackageManager.PERMISSION_GRANTED
                        &&  grantResults[ACCESS_COARSE_LOCATION_INDEX] ==  PackageManager.PERMISSION_GRANTED ) {
                    //process PERMISSION_GRANTED
                    AppLog.i(TAG, "onRequestPermissionsResult PERMISSION_GRANTED");
                    mIsPermission = ApplicationDefine.MAIN_PERMISSION_GRANTED;
                }
                else {
                    //process PERMISSION_DENIED
                    AppLog.i(TAG, "onRequestPermissionsResult PERMISSION_GRANTED");
                    mIsPermission = ApplicationDefine.MAIN_PERMISSION_DENIED;
                }
            }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StepSensorManager.getInstance(this).UnsetSensorCallback(null);

        NormalItem pNormalItem;
        pNormalItem = NormalItemDBManager.getInstance(this).getRunItemFromDB();
        if( pNormalItem != null && pNormalItem.getDistance() > 0 ) {
            startService(new Intent(this, MiniService.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocationPermition();
    }

    @Override
    protected void onResume() {
        //Process End MiniService
        stopService(new Intent(this, MiniService.class));
        super.onResume();
    }

    public static int GetPermissionState() {
        return mIsPermission;
    }
}
