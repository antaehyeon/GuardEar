package com.imaginecup.ensharp.guardear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MainService extends Service {
    private NotificationManager mNotificationManager;
    private MainServiceThread mMainServiceThread;
    private Notification mNotification;
    private myServiceHandler mHandler;
    private SharedPreferences mPref;
    private static final int SEND_THREAD_START_MESSAGE = 0;
    private static final int SEND_THREAD_STOP_MESSAGE = 1;
    private static final int SEND_THREAD_NORMALEARPHONE_PLUGGED = 2;
    private static final int SEND_THREAD_NORMALEARPHONE_UNPLUGGED = 3;
    private static final int SEND_THREAD_BLUETOOTHEARPHONE_CONNECTED = 4;
    private static final int SEND_THREAD_BLUETOOTHEARPHONE_UNCONNECTED = 5;
    private static final int SEND_MUSIC_INFORMATION= 6;
    private static final int SEND_LISTENING_ELAPSE = 7;

    public MainService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPref = new SharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("메인서비스 onStartCommand","mainservice여기 옴");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mHandler = new myServiceHandler();
        mMainServiceThread = new MainServiceThread(mHandler, this);
        mMainServiceThread.start();
        mPref.putValue("0",true,"service");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mMainServiceThread.stopRunning();
        mNotificationManager.cancel(0);
        //unregisterReceiver(headSetConnectReceiver);
        mMainServiceThread = null;
        mPref.putValue("0",false,"service");
        Log.i("메인서비스 onDestroy 실행","종료");
        Toast.makeText(getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i("노티비코드", "handleMessage");
            switch (msg.what) {

                case SEND_THREAD_START_MESSAGE:
                    Intent intent = new Intent(MainService.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MainService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    mNotification = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Guardear")
                            .setContentText("서비스 On")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .build();

                    mNotification.flags = Notification.FLAG_NO_CLEAR;
                    mNotificationManager.notify(0, mNotification);
                    break;

                case SEND_THREAD_STOP_MESSAGE:
                    break;

                case SEND_THREAD_NORMALEARPHONE_PLUGGED :
                    MainActivity.setMainUiText("이어폰","O");
                    break;

                case SEND_THREAD_NORMALEARPHONE_UNPLUGGED :
                    MainActivity.setMainUiText("이어폰","X");
                    break;

                case SEND_THREAD_BLUETOOTHEARPHONE_CONNECTED :
                    MainActivity.setMainUiText("블루투스 이어폰","O");
                    break;

                case SEND_THREAD_BLUETOOTHEARPHONE_UNCONNECTED :
                    MainActivity.setMainUiText("블루투스 이어폰","X");
                    break;

                case SEND_MUSIC_INFORMATION :
                    Log.i("핸들러에서의 스트링값",msg.obj.toString());
                    MainActivity.setMainUiText("음악 재생 정보", msg.obj.toString());
                    break;

//                case SEND_LISTENING_ELAPSE :
//                    Log.i("핸들러에서의 청취 시간",msg.obj.toString());
//                    MainActivity.setMainUiText("음악 청취 시간", msg.obj.toString());
//                    break;
            }
        }
    }
}
