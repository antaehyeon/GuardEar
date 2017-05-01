package com.imaginecup.ensharp.guardear;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static TextView earphoneTxt;
    public static TextView bluetoothEarphoneTxt;
    public static TextView musicOnTxt;
    public static TextView elapseTxt;
    public static TextView decibelTxt;
    public static TextView volumeTxt;
    public static TextView isPlayingTxt;
    public static TextView earphoneModelTxt;
    public static FrameLayout mRedCircleLayout;
    public static FrameLayout mSkyCircleLayout;
    public static FrameLayout mNormalCircleLayout;
    public static FrameLayout mCircleParentLayout;
    public static SharedPreferences sPref;

    private static final String BLUETOOTH_HEADSET_ACTION = "android.bluetooth.headset.action.STATE_CHANGED";
    private static final String BLUETOOTH_HEADSET_STATE = "android.bluetooth.headset.extra.STATE";
    private static final int LIMIT_DECIBEL = 70;
    private static IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    private static BroadcastReceiver mBroadcastReceiver = null;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothHeadset mBluetoothHeadset;

    private ImageButton decibelInfoBtn;
    private Button serviceBtn;
    public boolean isServiceOn;
    public SharedPreferences pref;
    private ServiceData mServiceData;
    public static ServiceData sServiceData;
    private Context mContext;
    private AudioManager mAudiomanager;
    private Toolbar mToolbar;

    private SharedPreferences mPref;

    android.content.SharedPreferences setting;
    android.content.SharedPreferences.Editor editor;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    /**
     * 음악 정보 test 중
     **/
    private MobileServiceClient mClient;
    private MobileServiceTable<MusicInfo> mMusicTable;
    private MusicInfoAdapter mAdapter;
    /**
     * 타이머 함수 test 중
     **/
    private final Handler handler = new Handler();
    private TimerTask second;
    int timer_sec;
    String timer_text;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("처음부터 시작", "onCreate 실행");
        earphoneTxt = (TextView) findViewById(R.id.earphoneTxt);
        //bluetoothEarphoneTxt = (TextView) findViewById(R.id.bluetoothTxt);
        //musicOnTxt = (TextView) findViewById(R.id.musicOnTxt);
        elapseTxt = (TextView) findViewById(R.id.elapseTxt);
        decibelTxt = (TextView) findViewById(R.id.decibelsTxt);
        volumeTxt = (TextView) findViewById(R.id.volumeTxt);
        earphoneModelTxt = (TextView) findViewById(R.id.earphoneModelTxt);
        isPlayingTxt = (TextView) findViewById(R.id.isPlayingTxt);
        mNormalCircleLayout = (FrameLayout) findViewById(R.id.normalCircleLayout);
        mSkyCircleLayout = (FrameLayout) findViewById(R.id.skyCircleLayout);
        mRedCircleLayout = (FrameLayout) findViewById(R.id.redCircleLayout);
        mCircleParentLayout = (FrameLayout) findViewById(R.id.circleParentLayout);
        sPref = new SharedPreferences(this);
        decibelInfoBtn = (ImageButton) findViewById(R.id.decibelsInfoBtn);
        serviceBtn = (Button) findViewById(R.id.serviceBtn);
        mAudiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mContext = this;
        mServiceData = new ServiceData(mContext);
        sServiceData = new ServiceData(mContext);
        pref = new SharedPreferences(this);
        setMainUiInfo();
        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mServiceData.isMyServiceRunning(MainService.class)) {
                    Log.i("isRun여부", "true로 통과");
                    //Toast.makeText(getApplicationContext(),"서비스 시작",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainService.class);
                    serviceBtn.setText("서비스 종료");
                    changeCircle("sky");
                    startService(intent);
                } else {
                    //Toast.makeText(getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();
                    Intent intent_mainservice = new Intent(MainActivity.this, MainService.class);
                    Intent intent_listeningservice = new Intent(MainActivity.this, ListeningService.class);
                    Intent intent_decibelservice = new Intent(MainActivity.this, DecibelService.class);
                    stopService(intent_mainservice);
                    stopService(intent_listeningservice);
                    stopService(intent_decibelservice);
                    serviceBtn.setText("서비스 시작");
                    changeCircle("normal");
                }
            }
        });
        decibelInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DecibelInfoActivity.class);
                startActivity(intent);
            }
        });
        decibelDataSave();
        TextView mainTitleTxt = (TextView) findViewById(R.id.mainTitleTxt);
        mainTitleTxt.setText("홈");
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mToolbar.setTitle(null);
        //mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        try {

            mClient = new MobileServiceClient("http://guardear.azurewebsites.net", MainActivity.this);

            // Get the Mobile Service Table instance to use
            mMusicTable = mClient.getTable(MusicInfo.class);

            //testStart();
            // 보내기 test
            //testSend();
            sendData();

        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

    }

    public void testStart() {

        timer_sec = 0;

        Toast.makeText(getApplicationContext(), timer_text, Toast.LENGTH_SHORT).show();

        second = new TimerTask() {
            @Override
            public void run() {
                Log.i("TEST TIMER", "Timer start");
                checkItem();
                timer_sec++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);

    }

    public void checkItem() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // 데이터를 가져오는 리스트
                    //final List<MusicInfo> result = mMusicTable.execute().get();
                    // mToDoTable.where().field("complete").eq(false).execute().get();
                    final List<MusicInfo> result = mMusicTable.where().field("id").eq(timer_sec).execute().get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (MusicInfo item : result) {
                                timer_text = item.getValue().toString();
                                Toast.makeText(MainActivity.this, item.getValue().toString(), Toast.LENGTH_SHORT).show();
                                Log.d("순서확인중", " mMusicTable " + item.getID().toString());
                            }

                        }
                    });
                } catch (final Exception e) {
                    //createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        }.execute();
        //runAsyncTask(task);
    }

    public void testSend() {

        timer_sec = 0;

        second = new TimerTask() {
            @Override
            public void run() {
                Log.i("TEST TIMER", "Timer send");
                sendItem();
                timer_sec++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);

    }

    public void sendItem() {

        if (mClient == null) {
            return;
        }

        // Create a new item
        final MusicInfo item = new MusicInfo();
        Log.i("TEST TIMER", "데이터 저장 전");

        item.setID(Integer.toString(timer_sec)); // setText(mTextNewToDo.getText().toString());
        item.setSecond(Integer.toString(timer_sec));// setComplete(false);
        item.setValue(Integer.toString(timer_sec + 100));
        Log.i("TEST TIMER", "데이터 저장 후");

        // Insert the new item
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final MusicInfo entity = mMusicTable.insert(item).get();
                    /*if (!entity.isComplete()) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.add(entity);
                            }
                        });
                    }*/
                } catch (Exception exception) {
                    createAndShowDialog(exception, "Error");
                }
                return null;
            }
        }.execute();
    }

    private void sendData() {
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

        int seconds = 0;
        //Log.i("while문 가기 전", mPref.getValue(Integer.toString(seconds), "인식못함", keyname));
        for (seconds = 0; seconds < 1000; seconds++) {
            Log.i("서버에 음원데이터 전송", "ㄱㄱ");
            Log.i("sharedpreference값 출력", seconds + "초" + pref.getValue(Integer.toString(seconds), "인식못함", "어쿠스틱 콜라보너무 보고싶어"));

            // Create a new item
            final MusicInfo item = new MusicInfo();
            Log.i("TEST TIMER", "데이터 저장 전");

            item.setID("어쿠스틱 콜라보너무 보고싶어" + seconds); // setText(mTextNewToDo.getText().toString());
            item.setSecond(Integer.toString(seconds));// setComplete(false);
            item.setValue((pref.getValue(Integer.toString(seconds), "인식못함", "어쿠스틱 콜라보너무 보고싶어")));
            Log.i("TEST TIMER", "데이터 저장 후");

            // Insert the new item
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        final MusicInfo entity = mMusicTable.insert(item).get();
                    } catch (Exception exception) {
                        //createAndShowDialog(exception, "Error");
                    }
                    return null;
                }
            }.execute();


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
            if (pref.getValue(Integer.toString(seconds), "인식못함", "어쿠스틱 콜라보너무 보고싶어") == "인식못함") {
                Log.i("for문 아웃", pref.getValue(Integer.toString(seconds), "인식못함", "어쿠스틱 콜라보너무 보고싶어"));
                break;
            }
        }
//
//        } catch (MalformedURLException e) {
//            Log.i("음악정보-", "1'ㄱㄱ");
//
//            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
//        }

    }


    public void decibelDataSave() {
        if (pref.getValue("0", "0", "Note5") == "0") {
            for (int i = 0; i < 16; i++) {
                if (i == 0) {
                    pref.putValue(Integer.toString(i), "3.7", "Note5");
                } else if (i == 1) {
                    pref.putValue(Integer.toString(i), "4.78", "Note5");
                } else if (i == 2) {
                    pref.putValue(Integer.toString(i), "5.11", "Note5");
                } else if (i == 3) {
                    pref.putValue(Integer.toString(i), "6.27", "Note5");
                } else if (i == 4) {
                    pref.putValue(Integer.toString(i), "8.39", "Note5");
                } else if (i == 5) {
                    pref.putValue(Integer.toString(i), "13.28", "Note5");
                } else if (i == 6) {
                    pref.putValue(Integer.toString(i), "20.42", "Note5");
                } else if (i == 7) {
                    pref.putValue(Integer.toString(i), "31.73", "Note5");
                } else if (i == 8) {
                    pref.putValue(Integer.toString(i), "44.92", "Note5");
                } else if (i == 9) {
                    pref.putValue(Integer.toString(i), "55.75", "Note5");
                } else if (i == 10) {
                    pref.putValue(Integer.toString(i), "78", "Note5");
                } else if (i == 11) {
                    pref.putValue(Integer.toString(i), "110.43", "Note5");
                } else if (i == 12) {
                    pref.putValue(Integer.toString(i), "176", "Note5");
                } else if (i == 13) {
                    pref.putValue(Integer.toString(i), "279", "Note5");
                } else if (i == 14) {
                    pref.putValue(Integer.toString(i), "443", "Note5");
                } else if (i == 15) {
                    pref.putValue(Integer.toString(i), "558", "Note5");
                }
            }
            pref.putValue("ohm", "35", "earphone");
            pref.putValue("spl", "97", "earphone");
        }
    }

    public void setMainUiInfo() {

        earphoneModelTxt.setText(pref.getValue("earphone_model", "EO-BG920BBKG", "userinfo"));
        if (pref.getValue("0", false, "normalEarphone")) {
            earphoneTxt.setText("Connected");
            Log.i("사용중인 이어폰 종류", "일반");
        } else {
            if (pref.getValue("0", false, "bluetoothEarphone")) {
                earphoneTxt.setText("Connected");
                Log.i("사용중인 이어폰 종류", "블루투스");
            } else {
                earphoneTxt.setText("Disconnected");
                Log.i("사용중인 이어폰 종류", "사용안함");
            }
        }

        if (!mServiceData.isMyServiceRunning(MainService.class)) {
            serviceBtn.setText("서비스 시작");
            changeCircle("normal");
            Log.i("서비스 러닝여부", "X");
        } else {
            serviceBtn.setText("서비스 종료");
            changeCircle("sky");
            Log.i("서비스 러닝여부", "O");
        }

        if (!mServiceData.isMyServiceRunning(ListeningService.class)) {
            //musicOnTxt.setText(pref.getValue("0", "없음", "음악 재생 정보"));
            isPlayingTxt.setText("Stop");
            elapseTxt.setText(mServiceData.convertLongToHms(pref.getValue("todayListeningTime", 0, "todayInfo")));
            Log.i("리스닝 서비스 러닝여부", "X");
        } else {
            //musicOnTxt.setText(pref.getValue("0", "없음", "음악 재생 정보"));
            isPlayingTxt.setText("Playing");
            Log.i("서비스 러닝여부", "O");
        }

        if (!mServiceData.isMyServiceRunning(DecibelService.class)) {
            decibelTxt.setText("0");
        }

        int volume = mAudiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
        VolumeBroadcastReceiver.currentVolume = Integer.toString(volume);
        volumeTxt.setText(Integer.toString(volume));
    }


    public static void setMainUiText(String textName, String textContent) {

        switch (textName) {
            case "이어폰":
                Log.i("메인으로 넘어온 값", textContent + "?");
                if (earphoneTxt != null) {
                    if (textContent.equals("O")) {
                        earphoneTxt.setText("Connected");
                    } else {
                        earphoneTxt.setText("Disconnected");
                    }
                }
                break;
            case "블루투스 이어폰":
                Log.i("메인으로 넘어온 값", textContent + "?");
                if (earphoneTxt != null) {
                    if (textContent.equals("O")) {
                        earphoneTxt.setText("Connected");
                    } else {
                        earphoneTxt.setText("Disconnected");
                    }
                }
                break;

            case "음악 재생 정보":
                Log.i("메인으로 넘어온 값", textContent + "?");
                if (musicOnTxt != null) {
                    musicOnTxt.setText(textContent);
                }
                break;
            case "음악 청취 시간":
                //Log.i("메인으로 넘어온 값", textContent + "?");
                if (elapseTxt != null) {
                    elapseTxt.setText(textContent);
                }
                break;
            case "현재 데시벨":
                //Log.i("메인으로 넘어온 값", textContent + "?");
                if (decibelTxt != null) {
                    if (sServiceData.isMyServiceRunning(MainService.class)) {
                        if (Integer.parseInt(textContent) >= LIMIT_DECIBEL) {
                            changeCircle("red");
                        } else {
                            changeCircle("sky");
                        }
                        //Log.i("뜨는 값",textContent);
                        decibelTxt.setText(textContent);
                    } else {
                        changeCircle("normal");
                    }
                }
                break;
            case "현재 음량":
                if (volumeTxt != null) {
                    volumeTxt.setText(textContent);
                }
                break;
        }
    }

    public static void changeCircle(String mode) {
        if (mode.equals("normal")) {
            mNormalCircleLayout.setVisibility(View.VISIBLE);
            mSkyCircleLayout.setVisibility(View.INVISIBLE);
            mRedCircleLayout.setVisibility(View.INVISIBLE);
        } else if (mode.equals("sky")) {
            mNormalCircleLayout.setVisibility(View.INVISIBLE);
            mSkyCircleLayout.setVisibility(View.VISIBLE);
            mRedCircleLayout.setVisibility(View.INVISIBLE);
        } else {
            mNormalCircleLayout.setVisibility(View.INVISIBLE);
            mSkyCircleLayout.setVisibility(View.INVISIBLE);
            mRedCircleLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume 실행", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //내 정보 페이지
        if (id == R.id.action_personal_info) {
            Intent intent = new Intent(this, MyInfo.class);
            startActivity(intent);
            return true;
        }
        //청력측정 페이지
        else if (id == R.id.action_hearing_test) {
            Intent intent = new Intent(getApplicationContext(), AudioMetryActivity.class);

            startActivity(intent);
            finish();

        }
        //로그아웃 페이지
        else if (id == R.id.action_logout) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

            //editor.clear(); //clear all stored data
            //editor.commit();
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(getApplicationContext(), MainLoginActivity.class);

            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        Log.d("태그", "AsyncTask");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();

        }
    }

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
        Log.d("태그", "createAndShowDialog2");


    }


}
