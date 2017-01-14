package com.rigo.noo.util;

import android.util.Log;

/**
 * Created by kbg82 on 2017-01-15.
 */

public class AppLog {
    private final static boolean mDebug = true;
    private final static String APPTAG = "NOO_";

    public static void i(String aTag, String aMsg) {
        if(mDebug) {
            Log.i(APPTAG+aTag, aMsg);
        }
    }

    public static void d(String aTag, String aMsg) {
        if(mDebug) {
            Log.d(APPTAG+aTag, aMsg);
        }
    }

    public static void e(String aTag, String aMsg) {
        if(mDebug) {
            Log.e(APPTAG+aTag, aMsg);
        }
    }

}
