package com.imaginecup.ensharp.guardear;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {


    private MobileServiceClient mClient;
    private MobileServiceTable<ToDoItem> mToDoTable;
    private com.imaginecup.ensharp.guardear.SharedPreferences mPref;
    private CallbackManager callbackManager;
    //private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 9001;
    private TextView mStatusTextView;
    private ToDoItemAdapter mAdapter;
    /* google */
    GoogleApiClient mGoogleApiClient;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1;

    EditText etEmail, etPassword;
    Button btn_login, btn_findPW;
    Button loginButton;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    String strMail;
    String strPW;
    String[] strArray;

    Boolean isCheck;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // facebookSDK 초기화 작업
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        //로그인 요청 부분
        callbackManager = CallbackManager.Factory.create();

        etEmail = (EditText) findViewById(R.id.etEmail); // e-mail
        etPassword = (EditText) findViewById(R.id.etPassword); // password
        btn_login = (Button) findViewById(R.id.btn_login); // login
        btn_findPW = (Button) findViewById(R.id.btnFindPW);
        loginButton = (Button) findViewById(R.id.login_button); // facebook

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // 연결에 실패했을 경우 실행되는 메서드입니다.
                    }
                })
                // 필요한 api가 있으면 아래에 추가
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btn_findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), FindpwActivity.class);
                startActivity(intent);
            }
        });
        // 구글로 로그인

        Button googleLoginButton = (Button) findViewById(R.id.btn_google);
        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 구글 로그인 화면을 출력합니다. 화면이 닫힌 후 onActivityResult가 실행됩니다.
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RESOLVE_CONNECTION_REQUEST_CODE);


                Intent intent = new Intent(LoginActivity.this, CompanyTypeActivity.class);
                startActivity(intent);

                finish();

            }
        });

        try {
            mClient = new MobileServiceClient("https://guardear.azurewebsites.net", LoginActivity.this);

            mToDoTable = mClient.getTable(ToDoItem.class);
            mAdapter = new ToDoItemAdapter(this, R.layout.row_earphone);
            mPref = new com.imaginecup.ensharp.guardear.SharedPreferences(this);

            setting = getSharedPreferences("setting", 0);
            editor = setting.edit();


        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // facebook login button click
    public void facebookClick(View view) {

        //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.e("페이스북", "onSuccess");
                // 사용자 정보 획득

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("result", object.toString()); // 페이스북 로그인 결과

                                try {
                                    // 이메일, 이름, 성별, 나이 비밀번호
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    String gender = object.getString("gender");
                                    String pw = "0000";
                                    String age = "23";

                                    Log.d("TAG", "페이스북 이메일 : " + email);
                                    Log.d("TAG", "페이스북 이름 : " + name);
                                    Log.d("TAG", "페이스북 성별 : " + gender);

                                    // Create a new item
                                    final ToDoItem item = new ToDoItem(pw, email, name, age, gender);

                                    Log.d("페이스북 서버", "정보 서버로 저장 ");

                                    // 페이스북 사용자 정보 저장(프리퍼런스)
                                    mPref.putValue("Information", item.toString(), "User");
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
                                                //createAndShowDialogFromTask(e, "Error");
                                            }
                                            return null;
                                        }
                                    };

                                    runAsyncTask(task);

                                    Intent intent = new Intent(getApplicationContext(), EarphoneActivity.class);
                                    startActivity(intent);

                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
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
    public void LoginClick(View view) {

        strMail = etEmail.getText().toString(); // e-mail
        strPW = etPassword.getText().toString(); // password

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // 데이터를 가져오는 리스트
                    final List<ToDoItem> result = mToDoTable.where().field("id").eq(strMail).execute().get();
                    Log.d("로그인 확인중", result.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (ToDoItem item : result) {

                                Log.d("로그인 확인중 id:", item.getId().toString() + "<<");
                                Log.d("로그인 확인중 pw", item.getPassword().toString() + "<<");
                                if (item.getId().toString().equals(strMail) && item.getPassword().toString().equals(strPW)) {
                                    Toast.makeText(LoginActivity.this, "로그인 되었습니다", Toast.LENGTH_SHORT).show();
                                    Log.d("로그인", "로그인 성공");
                                    Intent intent = new Intent(getApplicationContext(), EarphoneActivity.class);

                                    startActivity(intent);
                                    finish();
                                } else if (!item.getId().toString().equals(strMail)) {

                                    editor.clear();
                                    editor.commit();

                                    Log.d("로그인", "아이디 실패");
                                    Log.d("로그인", strArray[0].toString());
                                    etEmail.setText(null);
                                    etPassword.setText(null);
                                    Toast.makeText(LoginActivity.this, "등록된 아이디가 없습니다", Toast.LENGTH_SHORT).show();
                                } else if (!item.getPassword().toString().equals(strPW)) {
                                    editor.clear();
                                    editor.commit();

                                    etPassword.setText(null);
                                    Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                    Log.d("로그인", "비밀번호 실패");

                                } else {
                                    Toast.makeText(LoginActivity.this, "존재하지 않습니다", Toast.LENGTH_SHORT).show();


                                }
                            }

                        }
                    });


                } catch (final Exception e) {
                    //createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        }.execute();



       /* AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //final List<ToDoItem> result = mToDoTable.where().field("name").eq(strMail).execute().get();

                String test;
                test = mPref.getValue("Login", "", "ID");

                strArray = test.toString().split("/");
                Log.d("프리퍼런스", test.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (Looper.myLooper() == null) {
                            Looper.prepare();
                        }

                        if (strMail.equals(strArray[0]) && strPW.equals(strArray[1])) {
                            isCheck = true;

                            editor.putString("ID", strMail);
                            editor.putString("PW", strPW);
                            editor.putBoolean("Auto_Login", true);
                            editor.commit();

                            Toast.makeText(LoginActivity.this, "로그인 되었습니다", Toast.LENGTH_SHORT).show();
                            Log.d("로그인", "로그인 성공");
                            Intent intent = new Intent(getApplicationContext(), EarphoneActivity.class);

                            startActivity(intent);
                            finish();

                        } else if (!strMail.equals(strArray[0])) {
                            isCheck = false;

                            editor.clear();
                            editor.commit();

                            Log.d("로그인", "아이디 실패");
                            Log.d("로그인", strArray[0].toString());
                            etEmail.setText(null);
                            etPassword.setText(null);
                            Toast.makeText(LoginActivity.this, "등록된 아이디가 없습니다", Toast.LENGTH_SHORT).show();

                        } else if (!strPW.equals(strArray[1])) {
                            isCheck = false;

                            editor.clear();
                            editor.commit();

                            etPassword.setText(null);
                            Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                            Log.d("로그인", "비밀번호 실패");
                        }
                        Looper.loop();
                    }
                });


                return null;
            }
        };
        runAsyncTask(task);*/


    }

    // callbackManager 호출
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 페이스북 로그인 결과를 콜백매니저에 담는다
        callbackManager.onActivityResult(requestCode, resultCode, data);
        switch ( requestCode )
        {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
                if ( result.isSuccess( ) ) {
                    GoogleSignInAccount acct = result.getSignInAccount( ); // 계정 정보 얻어오기

                    /* 자동 로그인 정보 저장*/

                    editor.putString("ID", acct.getEmail());
                    editor.putString("PW", "0000");
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();

                    Log.i("GOOGLE" , acct.getDisplayName( ) +" " );
                    Log.i("GOOGLE" , acct.getEmail( ) +" " );
                }
                break;
            default:
                super.onActivityResult( requestCode, resultCode, data );
        }

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
        builder.create().show();
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


    /*@Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("TAG", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }*/


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
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

}
