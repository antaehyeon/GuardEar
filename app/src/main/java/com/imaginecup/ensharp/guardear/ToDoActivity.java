package com.imaginecup.ensharp.guardear;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class ToDoActivity extends Activity {

    /**
     * Mobile Service Client reference
     */
    private MobileServiceClient mClient;

    /**
     * Mobile Service Table used to access data
     */
    private MobileServiceTable<ToDoItem> mToDoTable;

    //Offline Sync
    /**
     * Mobile Service Table used to access and Sync data
     */
    //private MobileServiceSyncTable<ToDoItem> mToDoTable;

    /**
     * Adapter to sync the items list with the view
     */
    private ToDoItemAdapter mAdapter;

    /**
     * EditText containing the "New To Do" text
     */
    private EditText mTextNewToDoID; // email
    private EditText mTextNewToDo;
    private EditText mTextNewToDo2; //password confirm
    private EditText mTextNewToDOName;

    private Spinner mSpinnerAge;
    private RadioGroup radioGroup;
    private RadioButton redio_female;
    private RadioButton radio_male;
    private String select_age;
    private String select_sex;

    private SharedPreferences mPref;
    android.content.SharedPreferences setting;
    android.content.SharedPreferences.Editor editor;

    Button btn_check;

    boolean checked;
    String mail;

    /**
     * Progress spinner to use for table operations
     */
    private ProgressBar mProgressBar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Initializes the activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        Log.d("태그", "TodoActivity 화면 전환 2");

        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        mTextNewToDoID = (EditText) findViewById(R.id.textNewToDoID); //id
        mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);
        mTextNewToDo2 = (EditText) findViewById(R.id.textNewToDo2);
        mTextNewToDOName = (EditText) findViewById(R.id.textNewToDoName);
        mSpinnerAge = (Spinner) findViewById(R.id.sp_age);
        radioGroup = (RadioGroup) findViewById(R.id.radio_sex);
        btn_check = (Button) findViewById(R.id.btn_check);



        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Age, android.R.layout.simple_spinner_item);
        
        //스피너와 어댑터 연결
        mSpinnerAge.setAdapter(adapter);



        // Initialize the progress bar
        mProgressBar.setVisibility(ProgressBar.GONE);

        mPref = new SharedPreferences(this);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();


        try {
            Log.d("Error test", "try in");
            mClient = new MobileServiceClient("http://guardear.azurewebsites.net", ToDoActivity.this).withFilter(new ProgressFilter());
            Log.d("Error test", "try suc.");
            // Extend timeout from default of 10s to 20s
           /* mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });*/
            // Get the Mobile Service Table instance to use
            mToDoTable = mClient.getTable(ToDoItem.class);
            Log.d("Error test", "mToDoTable suc.");


            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("회원가입 test", "onClick_checkItem");
                    checkItem();
                }
            });

            // 성별
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.d("Error test", "radioGroup");
                    // TODO Auto-generated method stub

                    RadioButton radioGroup = (RadioButton) findViewById(checkedId);

                    Toast.makeText(ToDoActivity.this, radioGroup.getText() + "체크", Toast.LENGTH_SHORT).show();
                    Log.d("라디오버튼", radioGroup.getText() + "체크");

                    switch (checkedId) {
                        case R.id.maile:
                            Toast.makeText(ToDoActivity.this, "남자 체크", Toast.LENGTH_SHORT).show();
                            select_sex = radioGroup.getText().toString();
                            Log.d("라디오버튼", select_sex);

                            break;
                        case R.id.female:
                            Toast.makeText(ToDoActivity.this, "여자 체크", Toast.LENGTH_SHORT).show();
                            select_sex = radioGroup.getText().toString();
                            Log.d("라디오버튼", select_sex);

                            break;
                        default:
                            break;
                    }
                }

            });


            // Create an adapter to bind the items with the view
            mAdapter = new ToDoItemAdapter(this, R.layout.row_list_to_do);

            // Load the items from the Mobile Service
            //refreshItemsFromTable();

            //이메일 형식 체크

            // 비밀번호 일치 검사
            mTextNewToDo2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    Log.d("태그", "비밀번호 일치 검사");

                    String password = mTextNewToDo.getText().toString();
                    String confirm = mTextNewToDo2.getText().toString();

                    if (password.equals(confirm)) {
                        mTextNewToDo.setTextColor(Color.WHITE);
                        mTextNewToDo2.setTextColor(Color.WHITE);
                    } else {
                        mTextNewToDo.setTextColor(Color.RED);
                        mTextNewToDo2.setTextColor(Color.RED);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            createAndShowDialog(e, "Error");
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ToDo Page") // TODO: Define a title for the content shown.
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

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            Toast.makeText(getApplicationContext(), "The planet is " +
                    parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }


    /**
     * Add a new item
     *
     * @param view The view that originated the call
     */
    public void addItem(View view) {

        Log.d("태그", "addItem 실행 ");
        Log.d("태그", "T?F : "+ checked );

        if (mClient == null) {
            return;
        }

        // 이메일 입력 확인
        if (mTextNewToDoID.getText().toString().length() == 0 || checkEmail(mTextNewToDoID.getText().toString()) == false) {
            checkEmail(mTextNewToDoID.getText().toString());
            Toast.makeText(ToDoActivity.this, "Email을 입력하세요!", Toast.LENGTH_SHORT).show();
            mTextNewToDoID.requestFocus();
            return;
        }

        // 비밀번호 입력 확인
        if (mTextNewToDo.getText().toString().length() == 0) {
            Toast.makeText(ToDoActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
            mTextNewToDo.requestFocus();
            return;
        }

        // 비밀번호 확인 입력 확인
        if (mTextNewToDo2.getText().toString().length() == 0) {
            Toast.makeText(ToDoActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
            mTextNewToDo2.requestFocus();
            return;
        }

        // 비밀번호 일치 확인
        if (!mTextNewToDo.getText().toString().equals(mTextNewToDo2.getText().toString())) {
            Toast.makeText(ToDoActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
            mTextNewToDo.setText("");
            mTextNewToDo2.setText("");
            mTextNewToDo.requestFocus();
            return;
        }

        if(checked == true){

            // Create a new item
            final ToDoItem item = new ToDoItem(mTextNewToDo.getText().toString(), mTextNewToDoID.getText().toString(),
                    mTextNewToDOName.getText().toString(), mSpinnerAge.getSelectedItem().toString(), select_sex);

            Log.d("태그프리퍼런스저장", item.toString());
            Log.d("태그프리퍼런스저장", mTextNewToDo.getText().toString());



            Log.d("태그", "정보 서버로 저장 ");

            // 회원가입 시 사용자 정보 임시 저장
            mPref.putValue("id", mTextNewToDoID.getText().toString(), "userinfo");
            mPref.putValue("pw", mTextNewToDo.getText().toString(), "userinfo");
            mPref.putValue("age", select_age, "userinfo");
            mPref.putValue("sex", select_sex, "userinfo");
            mPref.putValue("name", mTextNewToDOName.getText().toString(), "userinfo");


            //item.setText(mTextNewToDo.getText().toString());
            item.setComplete(false); // 내부 데이터 초기화


            // Insert the new item
            /*AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
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

            runAsyncTask(task);*/

            //입력 후 다시 공백칸으로
            mTextNewToDo.setText("");
            mTextNewToDoID.setText("");
            mTextNewToDOName.setText("");

            // 창 전환 -> LoginActivity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

            startActivity(intent);
            finish();
        }

    }

    //이메일 포맷 체크
    public static boolean checkEmail(String email) {

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }


    public void checkItem(){

        mail = mTextNewToDoID.getText().toString();

        Log.d("회원가입 test_mail : ", mail);
        if(checkEmail(mail)) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        // 데이터를 가져오는 리스트
                        final List<ToDoItem> result = mToDoTable.where().field("id").eq(mail).execute().get();
                        Log.d("회원가입 test", result.toString());

                        if(result.toString().equals("[]")){
                            checked = true;

                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ToDoActivity.this);

                            //builder.setTitle("제목 설정");
                            builder.setMessage("사용가능한 이름입니다");
                            Log.d("회원가입 setMessage", "setMessage 들어옴");
                            //확인 버튼 클릭 시 설정
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    return;
                                }
                            });
                            //알림창 객체 설정
                            android.support.v7.app.AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                for (ToDoItem item : result) {
                                    Log.d("회원가입 test_for", item.toString());

                                    //이메일이 있을때
                                    //if (item.getId().toString().equals(mail)) {

                                        Log.d("회원가입 test_if", item.getId().toString());

                                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ToDoActivity.this);

                                        //builder.setTitle("제목 설정");
                                        builder.setMessage("중복된 이름입니다");
                                        //확인 버튼 클릭 시 설정
                                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                return;
                                            }
                                        });
                                        //알림창 객체 설정
                                        android.support.v7.app.AlertDialog dialog = builder.create();
                                        dialog.show();
                                        // text 초기화
                                        mTextNewToDoID.setText(null);
                                        checked = false;

                                        return;

                                    //} else {


                                    //}
                                } //for

                            } //run()
                        });
                    } catch (final Exception e) {
                        //createAndShowDialogFromTask(e, "Error");
                    }
                    return null;
                }
            }.execute();
        }
        else{

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ToDoActivity.this);

            //builder.setTitle("제목 설정");
            builder.setMessage("잘못된 이메일입니다");
            //확인 버튼 클릭 시 설정
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    return;
                }
            });
            //알림창 객체 설정
            android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();


            return;
        }
    }

    // 취소버튼
    public void cancelItem(View view) {
        finish();
    }

    /**
     * Initializes the activity menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        Log.d("태그", "onCreateOptionsMenu 실행");

        return true;
    }

    /**
     * Select an option from the menu
     */
    @Override
    // 새로 고침 부분(REFRESH)
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            Log.d("태그", "새로고침 부분 ");

            refreshItemsFromTable();
        }

        return true;
    }


    /**
     * Mark an item as completed in the Mobile Service Table
     *
     * @param item The item to mark
     */
    public void checkItemInTable(ToDoItem item) throws ExecutionException, InterruptedException {
        mToDoTable.update(item).get();

    }

    /**
     * Add an item to the Mobile Service Table
     *
     * @param item The item to Add
     */
    public ToDoItem addItemInTable(ToDoItem item) throws ExecutionException, InterruptedException {
        ToDoItem entity = mToDoTable.insert(item).get();
        return entity;
    }

    /**
     * Refresh the list with the items in the Table
     */
    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<ToDoItem> results = refreshItemsFromMobileServiceTable();

                    //Offline Sync
                    //final List<ToDoItem> results = refreshItemsFromMobileServiceTableSyncTable();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();

                            for (ToDoItem item : results) {
                                mAdapter.add(item);
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
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     */

    private List<ToDoItem> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        return mToDoTable.where().field("complete").eq(val(false)).execute().get();
    }


    /**
     * Initialize local storage
     *
     * @return
     * @throws MobileServiceLocalStoreException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("id", ColumnDataType.String);
                    tableDefinition.put("text", ColumnDataType.String);
                    tableDefinition.put("complete", ColumnDataType.Boolean);

                    localStore.defineTable("ToDoItem", tableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };
        return runAsyncTask(task);
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

    /**
     * Run an ASync task on the corresponding executor
     *
     * @param task
     * @return
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        Log.d("태그", "AsyncTask");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();

        }
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }

    }


}

    /**
     * Mark an item as completed
     *
     * @param item
     *            The item to mark
     */
   /* public void checkItem(final ToDoItem item) { //삭제
        if (mClient == null) {
            return;
        }

        // Set the item as completed and update it in the table
        item.setComplete(true);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    checkItemInTable(item);
                    // 현재 UI thread가 아니기 때문에 runnable등록
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (item.isComplete()) {
                                mAdapter.remove(item);
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

    }
*/
    //Offline Sync
    /**
     * Sync the current context and the Mobile Service Sync Table
     * @return
     */
    /*
    private AsyncTask<Void, Void, Void> sync() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MobileServiceSyncContext syncContext = mClient.getSyncContext();
                    syncContext.push().get();
                    mToDoTable.pull(null).get();
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        return runAsyncTask(task);
    }
    */

    //Offline Sync
    /**
     * Refresh the list with the items in the Mobile Service Sync Table
     */
    /*private List<ToDoItem> refreshItemsFromMobileServiceTableSyncTable() throws ExecutionException, InterruptedException {
        //sync the data
        sync().get();
        Query query = QueryOperations.field("complete").
                eq(val(false));
        return mToDoTable.read(query).get();
    }*/


