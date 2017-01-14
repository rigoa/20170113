package com.rigo.noo.item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kbg82 on 2017-01-14.
 */

public class NormalItem implements Comparable<NormalItem>{

    private int mID;
    private String mStartTime;
    private float mDistance;
    private long mData;

    public NormalItem(){
        mID = -1;

        Calendar pCalendar = Calendar.getInstance();
        Date pDate = pCalendar.getTime();
        String sDate = new SimpleDateFormat("yyyy-MM-dd").format(pDate);

        mStartTime = sDate;
        mDistance = 0;
        mData = 0;
    }


    public NormalItem(String aStartTime, float aDistance, long aData){
        mID = -1;
        mStartTime = aStartTime;
        mDistance = aDistance;
        mData = aData;
    }

    public int getID()
    {
        return mID;
    }

    public String getStartTime(){
        return mStartTime;
    }

    public float getDistance(){
        return mDistance;
    }

    public long getData(){
        return mData;
    }


    public void setID(int aID)
    {
        mID = aID;
    }

    public void setStartTime(String aStartTime){
        mStartTime = aStartTime;
    }

    public void setDistance(float aDistance){ mDistance = aDistance; }

    public void setData(long aData){
        mData = aData;
    }

    @Override
    public int compareTo(NormalItem another) {
        // TODO Auto-generated method stub

        return 0;
    }
}
