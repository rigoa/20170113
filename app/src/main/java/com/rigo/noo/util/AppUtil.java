package com.rigo.noo.util;

/**
 * Created by kbg82 on 2017-01-15.
 */

public class AppUtil {

    static final int DISTANCE_KILOMETER = 1000;

    public static String DistanceFormat(long Parm1, float aParm2) {
        String pResult = "";

        double nCalc = (double)Parm1 * (double)aParm2;

        if( nCalc <  DISTANCE_KILOMETER)
            pResult = String.format("%.1fm", nCalc);
        else
            pResult = String.format("%.1fkm", nCalc/DISTANCE_KILOMETER);



        return pResult;
    }


}
