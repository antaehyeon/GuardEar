package com.imaginecup.ensharp.guardear;

import android.util.Log;

/**
 * Created by MinKyeong on 2017. 4. 26..
 */

public class MusicInfo {


    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId; // 노래index

    @com.google.gson.annotations.SerializedName("musicValue")
    private String mMusicValue; // 값

    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;


    public MusicInfo() {
        Log.d("음악 값 정보", "MusicInfo()");
    }

    @Override
    public String toString() {
        Log.d("음악값 정보", "값 :"+toString());

        //return getModelName();
        return getID() +  "/" + getValue();
    }


    /**
     * Initializes a new ToDoItem
     *
     * @param id
     *            The item valse
     * @param value
     *            The item id
     */

    public MusicInfo(String id, String value) {
        this.setID(id);
        this.setValue(value);
        Log.d("음악 정보", "this.반환값");
    }

    /**
     * Returns the item text
     */
    public String getID() {
        Log.d("음악 정보 겟" , "getID()");
        return mId;
    }
    public final void setID(String id) {
        Log.d("음악 정보 셋", "setID()");
        mId = id;
    }

    /**
     * Returns the item id
     */
    public String getValue() { return mMusicValue; }
    public final void setValue(String value) {
        mMusicValue = value;
    }


    public boolean isComplete() {
        return mComplete;
    }
    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MusicInfo && ((MusicInfo) o).mId == mId;
    }

}