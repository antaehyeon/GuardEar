package com.imaginecup.ensharp.guardear;

import android.util.Log;

/**
 * Created by MinKyeong on 2017. 5. 5..
 */

public class ListeningData {

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("id") // 외래키
    private String mId; // 이름

    @com.google.gson.annotations.SerializedName("date") // 기본키
    private String mDate; // 시작 날짜

    @com.google.gson.annotations.SerializedName("time_00")
    private int mTime_00; // 0시

    @com.google.gson.annotations.SerializedName("time_01")
    private int mTime_01; // 0시

    @com.google.gson.annotations.SerializedName("time_02")
    private int mTime_02; // 0시

    @com.google.gson.annotations.SerializedName("time_03")
    private int mTime_03; // 0시

    @com.google.gson.annotations.SerializedName("time_04")
    private int mTime_04; // 0시

    @com.google.gson.annotations.SerializedName("time_05")
    private int mTime_05; // 0시

    @com.google.gson.annotations.SerializedName("time_06")
    private int mTime_06; // 0시

    @com.google.gson.annotations.SerializedName("time_07")
    private int mTime_07; // 0시

    @com.google.gson.annotations.SerializedName("time_08")
    private int mTime_08; // 0시

    @com.google.gson.annotations.SerializedName("time_09")
    private int mTime_09; // 0시

    @com.google.gson.annotations.SerializedName("time_10")
    private int mTime_10; // 0시

    @com.google.gson.annotations.SerializedName("time_11")
    private int mTime_11; // 0시

    @com.google.gson.annotations.SerializedName("time_12")
    private int mTime_12; // 0시

    @com.google.gson.annotations.SerializedName("time_13")
    private int mTime_13; // 0시

    @com.google.gson.annotations.SerializedName("time_14")
    private int mTime_14; // 0시

    @com.google.gson.annotations.SerializedName("time_15")
    private int mTime_15; // 0시

    @com.google.gson.annotations.SerializedName("time_16")
    private int mTime_16; // 0시

    @com.google.gson.annotations.SerializedName("time_17")
    private int mTime_17; // 0시

    @com.google.gson.annotations.SerializedName("time_18")
    private int mTime_18; // 0시

    @com.google.gson.annotations.SerializedName("time_19")
    private int mTime_19; // 0시

    @com.google.gson.annotations.SerializedName("time_20")
    private int mTime_20; // 0시

    @com.google.gson.annotations.SerializedName("time_21")
    private int mTime_21; // 0시

    @com.google.gson.annotations.SerializedName("time_22")
    private int mTime_22; // 0시

    @com.google.gson.annotations.SerializedName("time_23")
    private int mTime_23; // 0시

    @com.google.gson.annotations.SerializedName("time_24")
    private int mTime_24; // 0시



    public ListeningData() {
        Log.d("청취정보", "ListeningData()");
    }

    @Override
    public String toString() {
        Log.d("청취정보", "toString()");

        //return getModelName();
        return getID() +  "/" + getDate() + "/" + getTime_00()+ "/" + getTime_01()+ "/" + getTime_02()+ "/" + getTime_03()+ "/" + getTime_04()+ "/" + getTime_05()
                + "/" + getTime_06()+ "/" + getTime_07()+ "/" + getTime_08()+ "/" + getTime_09()+ "/" + getTime_10()+ "/" + getTime_11()
                + "/" + getTime_12()+ "/" + getTime_13()+ "/" + getTime_14()+ "/" + getTime_15()+ "/" + getTime_16()+ "/" + getTime_17()
                + "/" + getTime_18()+ "/" + getTime_19()+ "/" + getTime_20()+ "/" + getTime_21()+ "/" + getTime_22()+ "/" + getTime_23()
                + "/" + getTime_24(); // 임시 변환
    }



    public ListeningData(String id, String date, int time_00, int time_01, int time_02, int time_03, int time_04, int time_05, int time_06, int time_07, int time_08, int time_09,
                         int time_10,  int time_11,  int time_12,  int time_13,  int time_14,  int time_15,  int time_16,  int time_17,  int time_18,
                         int time_19,  int time_20,  int time_21, int time_22, int time_23, int time_24) {
        this.setID(id);
        this.setDate(date);
        this.setTime_00(time_00);
        this.setTime_01(time_01);
        this.setTime_02(time_02);
        this.setTime_03(time_03);
        this.setTime_04(time_04);
        this.setTime_05(time_05);
        this.setTime_06(time_06);
        this.setTime_07(time_07);
        this.setTime_08(time_08);
        this.setTime_09(time_09);
        this.setTime_10(time_10);
        this.setTime_11(time_11);
        this.setTime_12(time_12);
        this.setTime_13(time_13);
        this.setTime_14(time_14);
        this.setTime_15(time_15);
        this.setTime_16(time_16);
        this.setTime_17(time_17);
        this.setTime_18(time_18);
        this.setTime_19(time_19);
        this.setTime_20(time_20);
        this.setTime_21(time_21);
        this.setTime_22(time_22);
        this.setTime_23(time_23);
        this.setTime_24(time_24);

        Log.d("청취정보", "this.반환값");
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

    public int getTime_00() {  return mTime_00;}
    public final void setTime_00(int time_00) {  mTime_00 = time_00;}

    public int getTime_01() {  return mTime_01;}
    public final void setTime_01(int time_01) {  mTime_01 = time_01;}
    public int getTime_02() {  return mTime_02;}
    public final void setTime_02(int time_02) {  mTime_02 = time_02;}
    public int getTime_03() {  return mTime_03;}
    public final void setTime_03(int time_03) {  mTime_03 = time_03;}
    public int getTime_04() {  return mTime_04;}
    public final void setTime_04(int time_04) {  mTime_04 = time_04;}
    public int getTime_05() {  return mTime_05;}
    public final void setTime_05(int time_05) {  mTime_05 = time_05;}
    public int getTime_06() {  return mTime_06;}
    public final void setTime_06(int time_06) {  mTime_06 = time_06;}
    public int getTime_07() {  return mTime_07;}
    public final void setTime_07(int time_07) {  mTime_07 = time_07;}
    public int getTime_08() {  return mTime_08;}
    public final void setTime_08(int time_08) {  mTime_08 = time_08;}
    public int getTime_09() {  return mTime_09;}
    public final void setTime_09(int time_09) {  mTime_09 = time_09;}
    public int getTime_10() {  return mTime_10;}
    public final void setTime_10(int time_10) {  mTime_10 = time_10;}
    public int getTime_11() {  return mTime_11;}
    public final void setTime_11(int time_11) {  mTime_11 = time_11;}
    public int getTime_12() {  return mTime_12;}
    public final void setTime_12(int time_12) {  mTime_12 = time_12;}
    public int getTime_13() {  return mTime_13;}
    public final void setTime_13(int time_13) {  mTime_13 = time_13;}
    public int getTime_14() {  return mTime_14;}
    public final void setTime_14(int time_14) {  mTime_14 = time_14;}
    public int getTime_15() {  return mTime_15;}
    public final void setTime_15(int time_15) {  mTime_15 = time_15;}
    public int getTime_16() {  return mTime_16;}
    public final void setTime_16(int time_16) {  mTime_16 = time_16;}
    public int getTime_17() {  return mTime_17;}
    public final void setTime_17(int time_17) {  mTime_17 = time_17;}
    public int getTime_18() {  return mTime_18;}
    public final void setTime_18(int time_18) {  mTime_18 = time_18;}
    public int getTime_19() {  return mTime_19;}
    public final void setTime_19(int time_19) {  mTime_19 = time_19;}
    public int getTime_20() {  return mTime_20;}
    public final void setTime_20(int time_20) {  mTime_20 = time_20;}
    public int getTime_21() {  return mTime_21;}
    public final void setTime_21(int time_21) {  mTime_21 = time_21;}
    public int getTime_22() {  return mTime_22;}
    public final void setTime_22(int time_22) {  mTime_22 = time_22;}
    public int getTime_23() {  return mTime_23;}
    public final void setTime_23(int time_23) {  mTime_23 = time_23;}
    public int getTime_24() {  return mTime_24;}
    public final void setTime_24(int time_24) {  mTime_24 = time_24;}

    @Override
    public boolean equals(Object o) {
        return o instanceof ListeningData && ((ListeningData) o).mId == mId;
    }

}
