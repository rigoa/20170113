package com.rigo.noo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rigo.noo.db.NormalItemDBManager;
import com.rigo.noo.item.NormalItem;
import com.rigo.noo.naver.NAPI;
import com.rigo.noo.sensor.StepSensorManager;
import com.rigo.noo.util.AppLog;
import com.rigo.noo.util.AppUtil;

/**
 * Created by kbg82 on 2017-01-13.
 */

public class UserActivity extends Activity implements StepSensorManager.SensorCallback, LocationListener{

    private static String TAG = UserActivity.class.getSimpleName();

    //Layout
    private TextView mTvStepCount;
    private TextView mTvDistance;
    private TextView mTvLocation;
    private Button mBtAction;

    private long mStepValue;

    //Location
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        InitLayout();
        InitAdress();

    }


    public void InitAdress()
    {
        AppLog.i(TAG, "InitAdress_start");
        mTvLocation.setText(R.string.activity_location_wait);

        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //requestPermissions();

            AppLog.i(TAG, "InitAdress not have permission");
            return  ;
        }

        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d(TAG, "isGPSEnabled=" + isGPSEnabled);
        Log.d(TAG, "isNetworkEnabled=" + isNetworkEnabled);

        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, this);

    }

    public void InitLayout() {
        mTvStepCount = (TextView)findViewById(R.id.tvStep);
        mTvDistance = (TextView)findViewById(R.id.tvDistance);
        mTvLocation = (TextView)findViewById(R.id.tvLoaction);
        mBtAction = (Button)findViewById(R.id.btAction);

        mBtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( (int)view.getTag() == 1) {
                    //Current Running to Stop
                    onActionToStop();
                }
                else {
                    //Current Stop to Running
                    onActionToStart();
                }
            }
        });
    }

    public void UpdateUI() {
        //Process Get Save StepValue
        NormalItem pNormalItem;
        pNormalItem = NormalItemDBManager.getInstance(getApplicationContext()).getRunItemFromDB();
        if( pNormalItem != null && pNormalItem.getDistance() > 0 )
        {
            //Current Start
            mBtAction.setTag(1);
            mStepValue = pNormalItem.getData();
            mBtAction.setText(R.string.button_stop);
            StepSensorManager.getInstance(getApplicationContext()).Start();
        }
        else
        {
            //Current Stop
            mBtAction.setTag(0);
            mStepValue = 0;
            mBtAction.setText(R.string.button_start);

        }
        AppLog.i(TAG, "InitLayout mStepValue : " + mStepValue);

        mTvStepCount.setText(mStepValue+"");
        mTvDistance.setText(AppUtil.DistanceFormat( mStepValue, (float)0.6));

    }

    public void updateStep()
    {
        mStepValue++;
        mTvStepCount.setText(mStepValue+"");
        mTvDistance.setText( AppUtil.DistanceFormat( mStepValue, (float)0.6) );

        NormalItem pNormalItem = new NormalItem();
        pNormalItem.setData(mStepValue);
        pNormalItem.setDistance((float)0.6);
        NormalItemDBManager.getInstance(getApplicationContext()).addRunItemToDB(pNormalItem);
    }

    public void onActionToStop() {

        NormalItem pNormalItem = new NormalItem();
        pNormalItem.setDistance( (float) 0.6 );
        pNormalItem.setData( Integer.parseInt( mTvStepCount.getText().toString()));

        NormalItemDBManager.getInstance(getApplicationContext()).addNormalItemToDB(pNormalItem);
        pNormalItem = new NormalItem();
        NormalItemDBManager.getInstance(getApplicationContext()).addRunItemToDB(pNormalItem);

        mBtAction.setText(R.string.button_start);
        mBtAction.setTag(0);
        mTvStepCount.setText("0");
        mStepValue = 0;

        //Process Calc Distance
        mTvDistance.setText(mStepValue*7+"m");

        StepSensorManager.getInstance(getApplicationContext()).Stop();

    }

    public void onActionToStart() {
        mBtAction.setText(R.string.button_stop);
        mBtAction.setTag(1);
        mTvStepCount.setText("0");

        //Process Calc Distance
        mTvDistance.setText(mStepValue*7+"m");

        NormalItem pNormalItem = new NormalItem();
        pNormalItem.setDistance((float)0.6);
        NormalItemDBManager.getInstance(getApplicationContext()).addRunItemToDB(pNormalItem);
        StepSensorManager.getInstance(getApplicationContext()).Start();

    }


    @Override
    public void CallbackFunction() {
        AppLog.i(TAG, "CallbackFunction");
        updateStep();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLog.i(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLog.i(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppLog.i(TAG, "onStart");
        StepSensorManager.getInstance(getApplicationContext()).SetSensorCallback(this);
        UpdateUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppLog.i(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLog.i(TAG, "onStop");

    }

    //Location Process
    @Override
    public void onLocationChanged(Location location) {
        AppLog.i(TAG, "onLocationChanged");
        double nLatitude = location.getLatitude();
        double nLongitude = location.getLongitude();
        AppLog.i(TAG, "nLatitude : " + nLatitude);
        AppLog.i(TAG, "nLongitude : " + nLongitude);

        mLatitude = nLatitude;
        mLongitude = nLongitude;


        new Thread(new Runnable() {
            @Override
            public void run() {

                NAPI pNAPI = new NAPI();
                String pResult = pNAPI.getAdress(mLongitude, mLatitude);

                AppLog.i(TAG, "getAdress : " + pResult);
                //Process update UI
                //mTvLocation.setText(result);
            }
        }).start();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
