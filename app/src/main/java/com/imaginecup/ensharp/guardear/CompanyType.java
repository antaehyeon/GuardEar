package com.imaginecup.ensharp.guardear;

import android.util.Log;

/**
 * Created by MinKyeong on 2017. 2. 26..
 */

public class CompanyType {

    @com.google.gson.annotations.SerializedName("id")
    private String mId; // 회사


    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;



    public CompanyType() { Log.d("회사명", "CompanyType()"); }


    public CompanyType(String id){
        this.setID(id);
        Log.d("회사명", "this. 반환값");
    }

    @Override
    public String toString(){
        return getID();
    }


    public String getID() {
        Log.d("회사명" , "getID()");
        return mId;
    }
    public final void setID(String company) {
        Log.d("회사", "setID()");
        mId = company;
    }


    public boolean isComplete() {
        return mComplete;
    }
    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CompanyType && ((CompanyType) o).mId == mId;
    }


    /**
     * 알파벳 이름으로 정렬

    public static final Comparator<CompanyType> ALPHA_COMPARATOR = new Comparator<CompanyType>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(CompanyType mListDate_1, CompanyType mListDate_2) {
            return sCollator.compare(mListDate_1.mTitle, mListDate_2.mTitle);
        }
    };*/
}
