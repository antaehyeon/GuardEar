package com.imaginecup.ensharp.guardear;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Semin on 2017-02-13.
 */
public class DecibelServiceThread extends Thread {

    private SoundFile mSoundFile;
    private String mTrackFullPath;
    private long mStartPosition;
    private Context mContext;
    private Activity mActivity;
    private long mMillis;
    private static final int RUN = 0;
    private static final int PAUSE = 1;
    private long mBaseTime;
    private SharedPreferences mPref;
    private int mSeconds;
    private String mDecibels;
    private String mKeyName;
    private AudioManager mAudioManager;
    private static final int MEASURE_DECIBEL = 91;
    private MobileServiceClient mClient;
    private MobileServiceTable<MusicInfo> mMusicTable;
    private int mode;

    public DecibelServiceThread() {
    }

    public DecibelServiceThread(String trackFullPath, long startPosition, String keyName, Context context) {
        this.mTrackFullPath = trackFullPath;
        this.mStartPosition = startPosition;
        this.mContext = context;
        this.mKeyName = keyName;
    }

    public void stopRunning() {
        synchronized (this) {
            controlElapse(PAUSE);
            Log.i("서비스 중지", "stopRunning");
        }
    }

    public void run() {
        mPref = new SharedPreferences(mContext);
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        Log.i("Run함수 실행", "실행");
        if (!mTrackFullPath.equals("스트리밍 음원")) {
            Log.i("모드0", "0");
            mode = 0;
            mSoundFile = new SoundFile();
            try {
                controlElapse(RUN);
                if (mPref.getValue(Integer.toString(mSeconds), "인식못함", mKeyName).equals("인식못함")) {
                    mSoundFile.create(mTrackFullPath, mKeyName, mContext);
                }
                //sendData(mKeyName);
            } catch (final Exception e) {
                Log.e("mSoundfile이 null", "사운드 파일 없음");
            }
            Log.i("서비스 run", "sendEmptyMessage");

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }
        } else {
            mode = 1;
            Log.i("모드1", "1");
            try {
                //controlElapse(RUN);

//                if (mPref.getValue(Integer.toString(mSeconds), "인식못함", mKeyName).equals("인식못함")) {
//                    //서버에서 받기
//                }
                Log.i("사이 진입", "진입");
                try {
                    Log.e("받아오려고 진입", "진입");
                    mClient = new MobileServiceClient("http://guardear.azurewebsites.net", mContext);

                    // Get the Mobile Service Table instance to use
                    mMusicTable = mClient.getTable(MusicInfo.class);

                    firstAction();
                    Log.e("firstAction종료", "종료");
                } catch (MalformedURLException e) {
                    createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
                }

            } catch (final Exception e) {
                Log.e("mSoundfile이 null", "사운드 파일 없음");
            }
            Log.i("서비스 run", "sendEmptyMessage");

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }
        }

    }

    private void sendData(String keyname){
        // Get the Mobile Service Table instance to use
//        Log.i("음악정보 try 전", "1ㄱㄱ");
//
//
//        try {
//            Log.i("음악정보", "1ㄱㄱ");
//            mClient = new MobileServiceClient("http://guardear.azurewebsites.net", mContext);
//            // Get the Mobile Service Table instance to use
//            Log.i("음악정보", "2ㄱㄱ");
//
//            mMusicTable = mClient.getTable(MusicInfo.class);
//            Log.i("음악정보", "3ㄱㄱ");
//
//            mMusicTable = mClient.getTable(MusicInfo.class);
//            Log.i("음악정보", "4ㄱㄱ");
//            // Create a new item
//            final MusicInfo item = new MusicInfo();
//            Log.i("5", "ㄱㄱ");
//            String decibels;

            int seconds=0;
            //Log.i("while문 가기 전", mPref.getValue(Integer.toString(seconds), "인식못함", keyname));
            for(seconds = 0; seconds<1000; seconds++){
                Log.i("서버에 음원데이터 전송", "ㄱㄱ");
                Log.i("sharedpreference값 출력", seconds+"초"+mPref.getValue(Integer.toString(seconds), "인식못함", keyname));
                //addItem(seconds, Double.valueOf(mPref.getValue(Integer.toString(seconds), "인식못함", keyname)), keyname);
                // 데이터 저장
//                decibels = mPref.getValue(Integer.toString(seconds), "인식못함", keyname);
//
//                item.setID(keyname);
//                item.setSecond(Integer.toString(seconds));
//                item.setValue(decibels);
//                // Insert the new item
//                new AsyncTask<Void, Void, Void>() {
//
//                    @Override
//                    protected Void doInBackground(Void... params) {
//                        try {
//                            final MusicInfo entity = mMusicTable.insert(item).get();
//                        } catch (Exception exception) {
//                            //createAndShowDialog(exception, "Error");
//                        }
//                        return null;
//                    }
//                }.execute();
//
//                // 값 확인
//                Log.d("서버로 데이터 저장", item.toString());
//                seconds++;
//                if(mPref.getValue(Integer.toString(seconds), "인식못함", keyname)=="인식못함") {
//                    Log.i("for문 아웃", mPref.getValue(Integer.toString(seconds), "인식못함", keyname));
//                    break;
//                }
           }
//
//        } catch (MalformedURLException e) {
//            Log.i("음악정보-", "1'ㄱㄱ");
//
//            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
//        }

    }

    //곡 정보 저장
    public void addItem(int seconds, double decibels, String keyname) {

        // Get the Mobile Service Table instance to use
        mMusicTable = mClient.getTable(MusicInfo.class);

        // Create a new item
        final MusicInfo item = new MusicInfo();

        item.setID(keyname);
        item.setSecond(Integer.toString(seconds));
        item.setValue(Double.toString(decibels));
        item.setComplete(false);
        Log.d("서버로 데이터 저장", item.toString());
        // Insert the new item
/*        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final MusicInfo entity = mMusicTable.insert(item).get();
                    if (!entity.isComplete()) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.add(entity);
                            }
                        });
                    }
                } catch (Exception exception) {
                    createAndShowDialog(exception, "Error");
                }
                return null;
            }
        }.execute();*/

    }

    public void controlElapse(int mode) {
        Log.i("시간측정", "시작");
        switch (mode) {
            case RUN:
                mBaseTime = SystemClock.elapsedRealtime();
                myTimer.sendEmptyMessage(0);
                break;
            case PAUSE:
                myTimer.removeMessages(0);
                Log.i("PAUSE", "보냄");
                break;
        }
    }

    public String getTimeOut() {
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
        mMillis = now - mBaseTime + mStartPosition;
        //String easy_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
        String ms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(mMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mMillis)),
                TimeUnit.MILLISECONDS.toSeconds(mMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mMillis)));
        mSeconds = (int) mMillis / 1000;
        if(mode==0){
            mDecibels = mPref.getValue(Integer.toString(mSeconds), "54", mKeyName);
        } else {
          // 여기다가 받는코드 넣으면 됨

            //  mDecibels =? mDecibels라는 데시벨 변수에 넣으면 됨
        }

        //Log.i("전 mDecibels=",mDecibels);
        mDecibels = getDecibel();
        //Log.i("후 mDecibels=",mDecibels);
        //return "현재 재생위치 : " + mSeconds + " / " + ms + " 현재 데시벨 : " + mDecibels;
        return mDecibels;
    }

    private void firstAction(){

        new AsyncTask<Activity, Void, Activity>(){
            @Override
            protected Activity doInBackground(Activity... params) {
                Log.d("이어폰", "결과값 확인 : doInBackground");
                return params[0];
            }

            //@Override
            protected void onPostExecute(Activity result) {
                //super.onPostExecute(result);
                Log.d("이어폰", "결과값 확인 : onPostExecute");
                getItemByAzure();
            }

        }.execute(this);
    }

    public void getItemByAzure(){

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.d("이어폰", "결과값 확인 : try");
                    // 데이터를 가져오는 리스트
                    final List<MusicInfo> result = mMusicTable.orderBy("seconds", QueryOrder.Ascending).top(50).execute().get();
                    final List<MusicInfo> result2 = mMusicTable.orderBy("seconds", QueryOrder.Ascending).skip(50).top(50).execute().get();
                    final List<MusicInfo> result3 = mMusicTable.orderBy("seconds", QueryOrder.Ascending).skip(100).top(50).execute().get();
                    final List<MusicInfo> result4 = mMusicTable.orderBy("seconds", QueryOrder.Ascending).skip(150).top(50).execute().get();
                    final List<MusicInfo> result5 = mMusicTable.orderBy("seconds", QueryOrder.Ascending).skip(200).top(50).execute().get();

                    Log.d("이어폰", "결과값 확인 : result");

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("이어폰", "런 들어옴");

                            if(Looper.myLooper() == null){ Looper.prepare();   }


                            for(MusicInfo item : result){
                                mDecibels = item.getValue().toString();

                                Log.d("음악정보확인1" ,item.getSecond().toString()+"초"+item.getValue().toString());
                            }
                            for(MusicInfo item2 : result2){
                                mDecibels = item2.getValue().toString();

                                Log.d("음악정보확인2", item2.getSecond().toString()+"초"+item2.getValue().toString());
                            }
                            for(MusicInfo item3 : result3){
                                mDecibels = item3.getValue().toString();

                                Log.d("음악정보확인3",item3.getSecond().toString()+"초"+item3.getValue().toString());
                            }
                            for(MusicInfo item4 : result4){
                                mDecibels = item4.getValue().toString();

                                Log.d("음악정보확인4", item4.getSecond().toString()+"초"+item4.getValue().toString());
                            }
                            for(MusicInfo item5 : result5){
                                mDecibels = item5.getValue().toString();

                                Log.d("음악정보확인5", item5.getSecond().toString()+"초"+item5.getValue().toString());
                            }
                            Looper.loop();
                        }
                    });
                } catch (final Exception e){
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        runAsyncTask(task);
    }


    public String getDecibel() {
        //W = (V * V)/R
        //dB = 10log(1/mW)
        double V, mW, dB, temp;
        int decibel;
        int currentVolume = Integer.parseInt(VolumeBroadcastReceiver.currentVolume);
        boolean isAutoControl = mPref.getValue("autovolume", false, "setting");
        //Log.i("getDecibel()함수", currentVolume+"");
        if (!isAutoControl) {
            //Log.i("볼륨컨트롤x", "dd");
            V = Double.parseDouble(mPref.getValue(VolumeBroadcastReceiver.currentVolume, "13.28", "Note5"));
            //Log.i("음량전압값 변화", " V = " + V + "  볼륨 : " + mPref.getValue("0", "5", "currentVolume"));
            mW = ((V * V) / Double.parseDouble(mPref.getValue("ohm", "35", "earphone"))) / 1000;
            dB = Math.round(10 * Math.log10(1 / mW));
            dB = Double.parseDouble(mPref.getValue("spl", "97", "earphone")) - dB;
            //이제부터 음원에 대한 계산
            temp = MEASURE_DECIBEL - Double.parseDouble(mDecibels);
            dB = Math.round(dB - temp);
            decibel = (int) dB;
            return Integer.toString(decibel);
        } else {
            //Log.i("볼륨컨트롤O", "dd");
            V = Double.parseDouble(mPref.getValue(Integer.toString(currentVolume), "13.28", "Note5"));
            //Log.i("음량전압값 변화", " V = " + V);
            mW = ((V * V) / Double.parseDouble(mPref.getValue("ohm", "35", "earphone"))) / 1000;
            //Log.i("음량전압값 변화", " mW = " + mW);
            dB = Math.round(10 * Math.log10(1 / mW));
            //Log.i("음량전압값 변화", " dB = " + dB);
            dB = Double.parseDouble(mPref.getValue("spl", "97", "earphone")) - dB;
            //Log.i("음량전압값 변화", " dB = " + dB);
            //이제부터 음원에 대한 계산
            temp = MEASURE_DECIBEL - Double.parseDouble(mDecibels);
            //Log.i("temp=",temp+" ");
            dB = Math.round(dB - temp);
            //Log.i("dB=",dB+" ");
            if (dB >= 70) {
                //Log.i("볼륨 80 이상", "줄이기");
                currentVolume--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND);
            }
            decibel = (int) dB;
            return Integer.toString(decibel);
        }

    }

    Handler myTimer = new Handler() {
        public void handleMessage(Message msg) {
            MainActivity.setMainUiText("현재 데시벨", getTimeOut());
            //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
            //Log.i("RUN","작동중");
            myTimer.sendEmptyMessage(0);
        }
    };


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        Log.d("태그", "AsyncTask");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();

        }
    }

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
        Log.d("태그", "createAndShowDialogFromTask");

    }


    /**
     * Creates a dialog and shows it
     *
     * @param exception The exception to show in the dialog
     * @param title     The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if (exception.getCause() != null) {
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
        Log.d("태그", "createAndShowDialog1");

    }

    /**
     * Creates a dialog and shows it
     *
     * @param message The dialog message
     * @param title   The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
        Log.d("태그", "createAndShowDialog2");


    }

}
