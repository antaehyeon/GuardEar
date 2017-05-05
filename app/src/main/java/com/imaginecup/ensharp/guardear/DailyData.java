package com.imaginecup.ensharp.guardear;

import android.util.Log;

/**
 * Created by MinKyeong on 2017. 5. 5..
 */

public class DailyData {

    //(id, date ) primary key
    @com.google.gson.annotations.SerializedName("id")
    private String mId; // 이름

    @com.google.gson.annotations.SerializedName("date")
    private String mDate; // 시작 날짜

    @com.google.gson.annotations.SerializedName("avg_dB")
    private int mAvg_dB; // 평균 데시벨

    @com.google.gson.annotations.SerializedName("avg_time")
    private int mAvg_time; // 평균 시간

    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;

    public DailyData() {
        Log.d("일별청취정보", "DailyData()");
    }

    @Override
    public String toString() {
        Log.d("일별청취정보", "toString()");

        //return getModelName();
        return getID() +  "/" + getDate() + "/" + getAvg_dB()+ "/" + getAvg_time(); // 임시 변환
    }



    public DailyData(String id, String date, int avg_dB, int avg_time) {
        this.setID(id);
        this.setDate(date);
        this.setAvg_dB(avg_dB);
        this.setAvg_time(avg_time);
        Log.d("일별청취정보", "this.반환값");
    }

    /**
     * Returns the item text
     */
    public String getID() {
        Log.d("청취정보" , "getID()");
        return mId;
    }
    public final void setID(String model) {
        Log.d("청취정보", "setID()");
        mId = model;
    }

    /**
     * Returns the item id
     */
    public String getDate() { return mDate; }
    public final void setDate(String date) {
        mDate = date;
    }

    public int getAvg_dB() {  return mAvg_dB;}
    public final void setAvg_dB(int avg_dB) {  mAvg_dB = avg_dB;}

    public int getAvg_time() {  return mAvg_time;}
    public final void setAvg_time(int avg_time) {  mAvg_time = avg_time;}

    public boolean isComplete() {
        return mComplete;
    }
    public void setComplete(boolean complete) {
        mComplete = complete;
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof DailyData && ((DailyData) o).mId == mId;
    }

}
