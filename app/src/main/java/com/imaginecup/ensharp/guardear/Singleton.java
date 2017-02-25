package com.imaginecup.ensharp.guardear;

import android.app.Application;

/**
 * Created by HYEON on 2017-02-25.
 */

public class Singleton extends Application{

    int [] freqLeftData = new int[7];
    int [] freqRightData = new int[7];


    // GET
    public int[] getFreqLeftData() {
        return freqLeftData;
    }

    public int[] getFreqRightData() {
        return freqRightData;
    }


    // SET
    public void setFreqLeftData(int a, int b, int c, int d, int e, int f, int g){
        freqLeftData[0] = a;
        freqLeftData[1] = b;
        freqLeftData[2] = c;
        freqLeftData[3] = d;
        freqLeftData[4] = e;
        freqLeftData[5] = f;
        freqLeftData[6] = g;
    }

    public void setFreqRightData(int a, int b, int c, int d, int e, int f, int g) {
        freqRightData[0] = a;
        freqRightData[1] = b;
        freqRightData[2] = c;
        freqRightData[3] = d;
        freqRightData[4] = e;
        freqRightData[5] = f;
        freqRightData[6] = g;
    }





}
