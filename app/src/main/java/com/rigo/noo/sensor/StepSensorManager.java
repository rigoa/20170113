package com.rigo.noo.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import com.rigo.noo.util.AppLog;

/**
 * Created by kbg82 on 2017-01-14.
 */

public class StepSensorManager implements SensorEventListener {
    private static String TAG = StepSensorManager.class.getSimpleName();

    //not use cache
    private volatile static  StepSensorManager mStepSensorManager = null;
    private volatile boolean mIsSensorStart;

    private SensorCallback mSensorCallback;



    public static StepSensorManager getInstance(Context context) {
        if (mStepSensorManager == null) {
            synchronized (StepSensorManager.class) {
                if (mStepSensorManager == null) {
                    mStepSensorManager = new StepSensorManager(context);
                }
            }
        }
        return mStepSensorManager;
    }

    public interface  SensorCallback {
        public void CallbackFunction();
    }

    public void SetSensorCallback( SensorCallback aSensorCallback) {

        if(aSensorCallback == null) {
            AppLog.i(TAG, "setSensorCallback aSensorCallback : null");
        }
        else {
            AppLog.i(TAG, "setSensorCallback : " + aSensorCallback.toString());
            mSensorCallback = aSensorCallback;
        }
    }

    public void UnsetSensorCallback( SensorCallback aSensorCallback) {

        if(mSensorCallback == aSensorCallback) {
            mSensorCallback = null;
        }
        else {
            if(aSensorCallback != null)
                AppLog.i(TAG, "unsetSensorCallback aSensorCallback : " + aSensorCallback.toString());
            else
                AppLog.i(TAG, "unsetSensorCallback aSensorCallback : null");
        }
    }

    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 800;

    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor accelerormeterSensor;



    public StepSensorManager(Context aContext) {
        mContext = aContext;
        mSensorManager = (SensorManager) aContext.getSystemService(Context.SENSOR_SERVICE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
            accelerormeterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        else {
            accelerormeterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

    }

    public void Start() {
        AppLog.i(TAG, "Start");
        if (accelerormeterSensor != null) {
            mIsSensorStart = true;
            mSensorManager.registerListener(this, accelerormeterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else
        {
            //error process
        }
    }

    public void Stop() {
        AppLog.i(TAG, "Stop");
        mIsSensorStart = false;
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        else
        {
            //error process
        }
    }

    public boolean IsStart()
    {
        return mIsSensorStart;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
        AppLog.i(TAG, "onAccuracyChanged_accuracy : " + accuracy);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        AppLog.i(TAG, "onSensorChanged_start Type : " + event.sensor.getType());

        if(mSensorCallback != null)
            mSensorCallback.CallbackFunction();
        else
            AppLog.i(TAG, "onSensorChanged mSensorCallback is null");

        //Sensor.TYPE_STEP_COUNTER

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    // count process
                }

                lastX = event.values[SensorManager.DATA_X];
                lastY = event.values[SensorManager.DATA_Y];
                lastZ = event.values[SensorManager.DATA_Z];
            }
        }
        else if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER)
        {
            // count process
        }

    }
}
