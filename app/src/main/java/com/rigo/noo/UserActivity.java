package com.rigo.noo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private String mLocation;

    private int mPermisionLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mLocation = getResources().getString(R.string.activity_location_wait);
        InitLayout();
        StartAdress();
    }


    //get Location from GPS, Network
    public void StartAdress()
    {
        AppLog.i(TAG, "InitAdress_start");

        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            AppLog.i(TAG, "InitAdress not have permission");
            return;
        }

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        AppLog.d(TAG, "isGPSEnabled=" + isGPSEnabled);
        AppLog.d(TAG, "isNetworkEnabled=" + isNetworkEnabled);

        //Process Need Gps and Mobile State
        if( !isGPSEnabled && !isNetworkEnabled)
            Toast.makeText(this, R.string.activity_location_gps_network_off, Toast.LENGTH_SHORT).show();


        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, this);

    }

    public void InitLayout() {
        mTvStepCount = (TextView)findViewById(R.id.tvStep);
        mTvDistance = (TextView)findViewById(R.id.tvDistance);
        mTvLocation = (TextView)findViewById(R.id.tvLoaction);
        mBtAction = (Button)findViewById(R.id.btAction);

        mTvLocation.setText(mLocation);

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

    //Current State update from DB
    public void UpdateUI() {
        //Process Get Save StepValue
        NormalItem pNormalItem;
        pNormalItem = NormalItemDBManager.getInstance(this).getRunItemFromDB();
        if( pNormalItem != null && pNormalItem.getDistance() > 0 )
        {
            //Current Start
            mBtAction.setTag(1);
            mStepValue = pNormalItem.getData();
            mBtAction.setText(R.string.button_stop);
            StepSensorManager.getInstance(this).Start();
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
        mTvDistance.setText(AppUtil.DistanceFormat( mStepValue, ApplicationDefine.STEP_METER));
    }

    //Use Senser Callback
    public void updateStep()
    {
        mStepValue++;
        mTvStepCount.setText(mStepValue+"");
        mTvDistance.setText( AppUtil.DistanceFormat( mStepValue, ApplicationDefine.STEP_METER) );

        NormalItem pNormalItem = new NormalItem();
        pNormalItem.setData(mStepValue);
        pNormalItem.setDistance(ApplicationDefine.STEP_METER);
        NormalItemDBManager.getInstance(this).addRunItemToDB(pNormalItem);
    }

    public void onActionToStop() {

        NormalItem pNormalItem = new NormalItem();
        pNormalItem.setDistance( (float) 0.6 );
        pNormalItem.setData( Integer.parseInt( mTvStepCount.getText().toString()));

        NormalItemDBManager.getInstance(this).addNormalItemToDB(pNormalItem);
        pNormalItem = new NormalItem();
        NormalItemDBManager.getInstance(this).addRunItemToDB(pNormalItem);

        mBtAction.setText(R.string.button_start);
        mBtAction.setTag(0);
        mTvStepCount.setText("0");
        mStepValue = 0;

        //Process Calc Distance
        mTvDistance.setText(mStepValue*7+"m");

        StepSensorManager.getInstance(this).Stop();

    }

    public void onActionToStart() {
        mBtAction.setText(R.string.button_stop);
        mBtAction.setTag(1);
        mTvStepCount.setText("0");

        //Process Calc Distance
        mTvDistance.setText( AppUtil.DistanceFormat(mStepValue, ApplicationDefine.STEP_METER) );

        NormalItem pNormalItem = new NormalItem();
        pNormalItem.setDistance(ApplicationDefine.STEP_METER);
        NormalItemDBManager.getInstance(this).addRunItemToDB(pNormalItem);
        StepSensorManager.getInstance(this).Start();

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
        StartAdress();

        //Need Process location permission alert check
        if( MainActivity.GetPermissionState() == ApplicationDefine.MAIN_PERMISSION_DENIED) {
            mLocation = getString(R.string.activity_location_reject);
            mTvLocation.setText(mLocation);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppLog.i(TAG, "onStart");
        //Register Senser Callback
        StepSensorManager.getInstance(this).SetSensorCallback(this);
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

    //Process Location Listener
    @Override
    public void onLocationChanged(Location location) {
        AppLog.i(TAG, "onLocationChanged");

        double nLongitude = location.getLongitude();
        double nLatitude = location.getLatitude();

        if(location != null)
            if( nLongitude == 0 || nLatitude == 0) {
                AppLog.d(TAG, "nLongitude nLatitude is 0");
            }
            else
            {
                new UpdateTask(nLongitude, nLatitude).execute();
            }
        else
            AppLog.d(TAG, "onLocationChanged Location is null");
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

    //Use Location Address update.
    private  class UpdateTask extends AsyncTask< Void, Void, Void>
    {
        private String mTaskLocation;
        private  double mTaskLongitude;
        private  double mTaskLatitude;

        public UpdateTask(double aLongitude, double aLatitude) {
            super();
            mTaskLongitude = aLongitude;
            mTaskLatitude = aLatitude;
            AppLog.i(TAG, "mTaskLongitude : " + aLongitude);
            AppLog.i(TAG, "mTaskLatitude : " + aLatitude);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AppLog.i(TAG, "onPreExecute");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AppLog.i(TAG, "onPostExecute mTaskLocation : " + mTaskLocation);

            if(mTaskLocation == null || mTaskLocation.isEmpty()) {
                //process empty location skip
            }
            else
            {
                mTvLocation.setText(mTaskLocation);
            }
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            AppLog.i(TAG, "onCancelled");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            AppLog.i(TAG, "onCancelled");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppLog.i(TAG, "doInBackground");

            //Process Gps to Address
            NAPI pNAPI = new NAPI();
            mTaskLocation = pNAPI.getAdress(mTaskLongitude, mTaskLatitude);
            AppLog.i(TAG, "mTaskLocation : " + mTaskLocation);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            AppLog.i(TAG, "onProgressUpdate");

        }
    }
}
