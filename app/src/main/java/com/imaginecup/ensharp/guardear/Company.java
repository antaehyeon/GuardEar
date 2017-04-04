package com.imaginecup.ensharp.guardear;

/**
 * Created by MinKyeong on 2017. 2. 18..
 */


import android.util.Log;

/**
 * Represents an item in a Earphone
 */
public class Company {
    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId; // 회사명


    @com.google.gson.annotations.SerializedName("url")
    private String mUrl; // 사진 주소



    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;


    public Company() {
        Log.d("회사명", "Company()");
    }

    @Override
    public String toString() {
        Log.d("회사명", "회사명 toString()");

        return getID();
        //return getID() +  "/" + getSoundPressure() + "/" + getImpedance(); // 임시 변환
    }


    /**
     * Initializes a new ToDoItem
     *
     * @param id
     *            The item id
     * @param url
     *            The item url
     */
    public Company(String id, String url) {
        this.setID(id);
        this.setUrl(url);
        Log.d("회사명", "this.반환값");
    }

    /**
     * Returns the item text
     */
    public String getID() {
        Log.d("회사명 겟" , "getID()");
        return mId;
    }
    public final void setID(String model) {
        Log.d("회사명 셋", "setID()");
        mId = model;
    }

    /**
     * Returns the item id
     */
    public String getUrl() { return mUrl; }
    public final void setUrl(String url) {
        mUrl = url;
    }


    public boolean isComplete() {
        return mComplete;
    }
    public void setComplete(boolean complete) {
        mComplete = complete;
    }

 //   @Override
 /*   public boolean equals(Object o) {
        return o instanceof Earphone && ((Earphone) o).mId == mId;
    }
*/
}