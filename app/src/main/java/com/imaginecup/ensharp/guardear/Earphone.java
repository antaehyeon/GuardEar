package com.imaginecup.ensharp.guardear;

/**
 * Created by MinKyeong on 2017. 2. 18..
 */


import android.util.Log;

/**
 * Represents an item in a Earphone
 */
public class Earphone {
    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId; // 모델

    @com.google.gson.annotations.SerializedName("soundpressure")
    private String mSoundpressure; // 음압

    @com.google.gson.annotations.SerializedName("impedance")
    private String mImpedance;   // 저항

    @com.google.gson.annotations.SerializedName("company")
    private String mCompany; // 회사명


    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;


    public Earphone() {
        Log.d("이어폰", "Earphone()");
    }

    @Override
    public String toString() {
        Log.d("이어폰", "toString()");

        return getID();
        //return getID() +  "/" + getSoundPressure() + "/" + getImpedance(); // 임시 변환
    }


    /**
     * Initializes a new ToDoItem
     *
     * @param id
     *            The item text
     * @param soundpressure
     *            The item id
     * @param impedance
     */
    public Earphone(String id, String soundpressure, String impedance, String company) {
        this.setID(id);
        this.setSoundPressure(soundpressure);
        this.setImpedance(impedance);
        this.setCompany(company);
        Log.d("이어폰", "this.반환값");
    }

    /**
     * Returns the item text
     */
    public String getID() {
        Log.d("이어폰 겟" , "getID()");
        return mId;
    }
    public final void setID(String model) {
        Log.d("이어폰 셋", "setID()");
        mId = model;
    }

    /**
     * Returns the item id
     */
    public String getSoundPressure() { return mSoundpressure; }
    public final void setSoundPressure(String soundPressure) {
        mSoundpressure = soundPressure;
    }

    public String getImpedance() {  return mImpedance;}
    public final void setImpedance(String impedance) {  mImpedance = impedance;}

    public String getCompany() { return mCompany;}
    public final void setCompany(String company) { mCompany = company; }


    public boolean isComplete() {
        return mComplete;
    }
    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Earphone && ((Earphone) o).mId == mId;
    }
}