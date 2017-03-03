package com.imaginecup.ensharp.guardear;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainLoginActivity extends AppCompatActivity {

    private MobileServiceClient mClient;
    private MobileServiceTable<ToDoItem> mToDoTable;
    private com.imaginecup.ensharp.guardear.SharedPreferences mPref;


    private CallbackManager callbackManager;
    //private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 9001;
    private ToDoItemAdapter mAdapter;

    android.content.SharedPreferences setting;
    android.content.SharedPreferences.Editor editor;

    Button btn_facebook;
    Button btn_google;
    Button btn_email;
    Button join_email;

    private static final String TAG = "AppPermission";
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // facebookSDK 초기화 작업
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        //FacebookSdk.sdkInitialize(MainLoginActivity.this);
        setContentView(R.layout.activity_main_login);
        checkPermission();
        Log.d("페이스북 창", "onCreate()");


        //로그인 요청 부분
        callbackManager = CallbackManager.Factory.create();

        btn_facebook = (Button) findViewById(R.id.btn_facebook);
        btn_google = (Button) findViewById(R.id.btn_google);
        btn_email = (Button) findViewById(R.id.btn_email);
        join_email = (Button) findViewById(R.id.join_email);

        try {
            mClient = new MobileServiceClient("https://safeear.azurewebsites.net", MainLoginActivity.this);

            mToDoTable = mClient.getTable(ToDoItem.class);
            mAdapter = new ToDoItemAdapter(this, R.layout.row_earphone);
            mPref = new com.imaginecup.ensharp.guardear.SharedPreferences(this);

            setting = getSharedPreferences("setting", 0);
            editor = setting.edit();

            Log.d("페이스북 창", "onCreate() -try ");



            String auto_id = mPref.getValue("id", "", "userinfo");
            String auto_pw = mPref.getValue("pw", "", "userinfo");

            // Create a new item
            //final ToDoItem item = new ToDoItem(pw, email, name, age , gender);

            Log.d("자동로그인", auto_id);
            Log.d("자동로그인", auto_pw);
            //Log.d("자동로그인", item.toString());
            //Log.d("자동로그인", "test : "+ item.getId());




            /*if(item.toString().equals("null/null/null/null/null")) {
            }
            else if(auto_id.equals(item.getId().toString())&& auto_pw.equals(item.getText().toString())){

                Toast.makeText(MainLoginActivity.this, "로그인 되었습니다", Toast.LENGTH_SHORT).show();
                Log.d("로그인", "로그인 성공");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                finish();
            }
            else{
                Log.d("자동로그인", "일치 값 없음");
            }*/



        } catch (MalformedURLException e) {
            Log.d("페이스북 창", "onCreate() - catch");
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }


    }
//
    // facebook login button click
    public void facebookClick(View view){

        //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
        LoginManager.getInstance().logInWithReadPermissions(MainLoginActivity.this, Arrays.asList("public_profile", "email","user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.e("페이스북", "onSuccess");
                Log.d("페이스북 창", "facebookClick()");
                // 사용자 정보 획득

                final GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("페이스북 창", "onCompleted()");
                                Log.v("result", object.toString()); // 페이스북 로그인 결과
                                if(response != null) {
                                    try {
                                        // 이메일, 이름, 성별, 나이 비밀번호
                                        String name = object.getString("name");
                                        String gender = object.getString("gender");
                                        String email = object.getString("email");
                                        String pw = "0000";
                                        String age = "23";

                                        Log.d("TAG", "페이스북 이메일 : " + email);
                                        Log.d("TAG", "페이스북 이름 : " + name);
                                        Log.d("TAG", "페이스북 성별 : " + gender);

                                        // Create a new item
                                        final ToDoItem item = new ToDoItem(pw, email, name, age, gender);

                                        Log.d("페이스북 서버", "정보 서버로 저장 ");

                                        // 페이스북 사용자 정보 저장(프리퍼런스)
                                        mPref.putValue("id", email, "userinfo");
                                        mPref.putValue("pw", pw, "userinfo");
                                        mPref.putValue("age", age, "userinfo");
                                        mPref.putValue("sex", gender, "userinfo");
                                        mPref.putValue("name", name, "userinfo");

                                        Log.d("페이스북 서버", item.toString());


                                        // 내부 데이터 초기화
                                        item.setComplete(false);

                                        // Insert the new item
                                        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Void... params) {
                                                try {
                                                    final ToDoItem entity = addItemInTable(item);

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (!entity.isComplete()) {
                                                                Log.d("태그", "Insert the new item ");

                                                                mAdapter.add(entity);
                                                            }
                                                        }
                                                    });
                                                } catch (final Exception e) {

                                                    createAndShowDialogFromTask(e, "Error");
                                                }
                                                return null;
                                            }
                                        };

                                        runAsyncTask(task);


                                        Intent intent = new Intent(MainLoginActivity.this, CompanyTypeActivity.class);
                                        startActivity(intent);

                                        finish();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                //parameters.putString("fields", "email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("페이스북", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("페이스북", "onError " + exception.getLocalizedMessage());
            }
        });

    }

    // login btn click
    public void LoginEmail(View view) {

        Intent intent = new Intent(getApplicationContext(),  LoginActivity.class);
        startActivity(intent);

        finish();

    }

    public void JoinEmail(View view) {

        Intent intent = new Intent(getApplicationContext(),  ToDoActivity.class);
        startActivity(intent);

        finish();

    }

    // callbackManager 호출
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 페이스북 로그인 결과를 콜백매니저에 담는다
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.d("페이스북 callbackManager", "onActivityResult()");

    }



    /**
     * Creates a dialog and shows it
     *
     * @param exception The exception to show in the dialog
     * @param title     The dialog title
     */
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }


    /**
     * Creates a dialog and shows it
     *
     * @param exception The exception to show in the dialog
     * @param title     The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Log.d("createAndShowDialog() ", "createAndShowDialog() 첫번째");

        Throwable ex = exception;
        if (exception.getCause() != null) {
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message The dialog message
     * @param title   The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {

        Log.d("createAndShowDialog()", "createAndShowDialog() 두번째");

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        //builder.create().show();
        MainLoginActivity.this.finish();
    }

    /**
     * Run an ASync task on the corresponding executor
     *
     * @param task
     * @return
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        Log.d("어씽크테스크()", "runAsyncTask()");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public ToDoItem addItemInTable(ToDoItem item) throws ExecutionException, InterruptedException {
        ToDoItem entity = mToDoTable.insert(item).get();
        return entity;
    }

    /**
     * Permission check.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        Log.i(TAG, "CheckPermission : " + checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            Log.e(TAG, "permission deny");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    // calendar task you need to do.
                } else {
                    Log.d(TAG, "Permission always deny");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }
}
