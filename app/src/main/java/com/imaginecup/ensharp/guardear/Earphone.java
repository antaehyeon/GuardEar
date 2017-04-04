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
    private String mId; // 이름


    @com.google.gson.annotations.SerializedName("companyName")
    private String mCompanyName; // 회사명


    @com.google.gson.annotations.SerializedName("modelName")
    private String mModelName; // 모델명

    @com.google.gson.annotations.SerializedName("impedance")
    private String mImpedance;   // 저항

    @com.google.gson.annotations.SerializedName("soundPressure")
    private String mSoundpressure; // 음압




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
    public Earphone(String id, String soundpressure, String impedance, String companyName, String modelName) {
        this.setID(id);
        this.setSoundPressure(soundpressure);
        this.setImpedance(impedance);
        this.setCompanyName(companyName);
        this.setModelName(modelName);
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

    public String getCompanyName() { return mCompanyName;}
    public final void setCompanyName(String companyName) { mCompanyName = companyName; }

    public String getModelName() { return mModelName;}
    public final void setModelName(String modelName) { mModelName = modelName; }

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