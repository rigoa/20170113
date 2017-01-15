package com.rigo.noo.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.rigo.noo.ApplicationDefine;
import com.rigo.noo.MainActivity;
import com.rigo.noo.R;
import com.rigo.noo.db.NormalItemDBManager;
import com.rigo.noo.item.NormalItem;
import com.rigo.noo.sensor.StepSensorManager;
import com.rigo.noo.util.AppLog;
import com.rigo.noo.util.AppUtil;


/**
 * Created by kbg82 on 2017-01-14.
 */

public class MiniService extends Service{
    private static String TAG = MiniService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private View mView;
    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;

    private TextView mTvMiniStep;
    private TextView mTvMiniDistance;

    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    private boolean isMove = false;
    private long mMiniStep;
    private static StepSensorManager mStepSensorManager;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        AppLog.i(TAG,  "onCreate");

        InitLayout();
        CheckRunning();

        mTvMiniStep.setText(mMiniStep+"");
        mTvMiniDistance.setText( AppUtil.DistanceFormat( mMiniStep, ApplicationDefine.STEP_METER));



    }

    private  void InitLayout() {
        //InitLayout
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.service_mini, null);
        mView.setOnTouchListener(mViewTouchListener);
        mTvMiniStep = (TextView) mView.findViewById(R.id.tvMiniStep);
        mTvMiniDistance = (TextView) mView.findViewById(R.id.tvMiniDistance);

        //Process make top window
        mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.TOP | Gravity.LEFT;
        mParams.gravity = Gravity.CENTER;

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(mView, mParams);
    }

    private void CheckRunning(){
        //Process Check Running
        NormalItem pNormalItem;
        pNormalItem = NormalItemDBManager.getInstance(getApplicationContext()).getRunItemFromDB();
        if( pNormalItem != null && pNormalItem.getDistance() > 0 )
        {
            //Current Start
            mMiniStep = pNormalItem.getData();
            StepSensorManager.getInstance(getApplicationContext()).Start();
        }
        else
        {
            //Current Stop
            mMiniStep = 0;
        }

    }

    private StepSensorManager.SensorCallback mSensorCallback = new StepSensorManager.SensorCallback() {
        @Override
        public void CallbackFunction() {
            AppLog.i(TAG, "Inner CallbackFunction");
            mMiniStep++;
            mTvMiniStep.setText(mMiniStep+"");
            mTvMiniDistance.setText( AppUtil.DistanceFormat( mMiniStep, ApplicationDefine.STEP_METER) );

            NormalItem pNormalItem = new NormalItem();
            pNormalItem.setData(mMiniStep);
            pNormalItem.setDistance(ApplicationDefine.STEP_METER);
            NormalItemDBManager.getInstance(getApplicationContext()).addRunItemToDB(pNormalItem);
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        AppLog.i(TAG,  "onStartCommand");

        mStepSensorManager = StepSensorManager.getInstance(getApplicationContext());
        mStepSensorManager.SetSensorCallback(mSensorCallback);
        StepSensorManager.getInstance(getApplicationContext()).Start();

        startForeground(ApplicationDefine.APP_NOO_NOTIFICATION_ID,  new Notification());
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        AppLog.i(TAG, "onDestroy");

        if( mStepSensorManager != null)
            mStepSensorManager.UnsetSensorCallback(null);
        if (mView != null) {
            mManager.removeView(mView);
            mView = null;
        }
    }

    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {

        private long preTiem = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mViewX = mParams.x;
                    mViewY = mParams.y;
                    break;

                case MotionEvent.ACTION_UP:
                    long curTime = System.currentTimeMillis();
                    if(curTime - preTiem < 500) {
                        AppLog.i(TAG, "onDoubleTab");
                        //preocess double tap
                        Intent pIntent = new Intent(getApplicationContext(), MainActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, pIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            contentIntent.send();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    preTiem = curTime;
                    break;

                case MotionEvent.ACTION_MOVE:
                    isMove = true;
                    int x = (int) (event.getRawX() - mTouchX);
                    int y = (int) (event.getRawY() - mTouchY);

                    final int num = 5;
                    if ((x > -num && x < num) && (y > -num && y < num)) {
                        isMove = false;
                        break;
                    }

                    mParams.x = mViewX + x;
                    mParams.y = mViewY + y;

                    mManager.updateViewLayout(mView, mParams);

                    break;
            }

            return true;
        }
    };

}
