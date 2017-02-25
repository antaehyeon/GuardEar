package com.imaginecup.ensharp.guardear;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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
    private EditText mTextNewToDoID;
    private EditText mTextNewToDo;
    private EditText mTextNewToDo2; //password confirm
    private EditText mTextNewToDOName;

    private Spinner mSpinnerAge;
    private RadioGroup radioGroup;
    private String select_age;
    private String select_sex;

    private com.imaginecup.ensharp.guardear.SharedPreferences mPref;

    Button btn_check;
    String convertStr;
    String[] strArray;

    /**
     * Progress spinner to use for table operations
     */
    private ProgressBar mProgressBar;

    /**
     * Initializes the activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        Log.d("태그", "TodoActivity 화면 전환 2");

        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        mTextNewToDoID = (EditText) findViewById(R.id.textNewToDoID);
        mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);
        mTextNewToDo2 = (EditText) findViewById(R.id.textNewToDo2);
        mTextNewToDOName = (EditText) findViewById(R.id.textNewToDoName);
        mSpinnerAge = (Spinner) findViewById(R.id.sp_age);
        radioGroup = (RadioGroup) findViewById(R.id.radio_sex);
        btn_check = (Button) findViewById(R.id.btn_check);


        // Initialize the progress bar
        mProgressBar.setVisibility(ProgressBar.GONE);

        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient("https://safeear.azurewebsites.net", ToDoActivity.this).withFilter(new ProgressFilter());

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });
            // Get the Mobile Service Table instance to use
            mToDoTable = mClient.getTable(ToDoItem.class);

            // Offline Sync
            //mToDoTable = mClient.getSyncTable("ToDoItem", ToDoItem.class);

            //Init local storage
            initLocalStore().get();


            AgeSpinner();

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
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

            mPref = new com.imaginecup.ensharp.guardear.SharedPreferences(this);

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
    }


    /**
     * Add a new item
     *
     * @param view The view that originated the call
     */
    public void addItem(View view) {

        Log.d("태그", "addItem 실행 ");

        if (mClient == null) {
            return;
        }

        // 이메일 입력 확인
        if (mTextNewToDoID.getText().toString().length() == 0) {
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

        // Create a new item
        final ToDoItem item = new ToDoItem(mTextNewToDo.getText().toString(), mTextNewToDoID.getText().toString(),
                mTextNewToDOName.getText().toString(), select_age, select_sex);

        Log.d("태그프리퍼런스저장", item.toString());
        Log.d("태그", "정보 서버로 저장 ");

        // 회원가입 시 사용자 정보 임시 저장
        mPref.putValue("id", mTextNewToDoID.getText().toString(), "userinfo");
        mPref.putValue("pw", mTextNewToDo.getText().toString(), "userinfo");
        mPref.putValue("age", select_age, "userinfo");
        mPref.putValue("sex", select_sex, "userinfo");
        mPref.putValue("nickname", mTextNewToDOName.getText().toString(), "userinfo");


        //item.setText(mTextNewToDo.getText().toString());
        item.setComplete(false); // 내부 데이터 초기화


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

        //입력 후 다시 공백칸으로
        mTextNewToDo.setText("");
        mTextNewToDoID.setText("");
        mTextNewToDOName.setText("");

        // 창 전환 -> LoginActivity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

        startActivity(intent);
        finish();

    }

    //이메일 포맷 체크
    public static boolean checkEmail(String email) {

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }


    // 중복 확인(닉네임)
    public void checkName(View view) throws ExecutionException, InterruptedException {

        convertStr = mTextNewToDOName.getText().toString();

        //mPref.removeAllPreferences("ID");

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<ToDoItem> result = mToDoTable.where().field("name").eq(convertStr).execute().get();
                    Log.d("태그", "중복확인 result 값 받아오기");


                    strArray = result.get(0).toString().split("/");

                    Log.d("태그", "결과값 확인 : " + result.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }

                        /*    String str = mTextNewToDOName.getText().toString() ;

                            // process incoming messages here
                            if(str.equals(strArray[2]) ){
                                Log.d("태그", "if(str.equals(result.toString()))");
                                Toast.makeText(ToDoActivity.this, "중복된 이름입니다 " + strArray[2] , Toast.LENGTH_SHORT).show();
                                mTextNewToDOName.setText(null);
                            }
                            else{
                                mPref.putValue("Login", strArray[0]+ "/" + strArray[1]+ "/" + strArray[2], "ID");
                                String test;
                                test = mPref.getValue("Login", "", "ID");
                                Log.d("태그", test);
                                Toast.makeText(ToDoActivity.this, "사용할 수 있는 이름입니다" + strArray[2] , Toast.LENGTH_SHORT).show();
                            } */
                            Looper.loop();
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

    /**
     * Age, Sex Spinner
     **/
    private void AgeSpinner() {
        ArrayAdapter<CharSequence> Adapter;
        Adapter = ArrayAdapter.createFromResource(ToDoActivity.this, R.array.spinner_age, android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAge.setAdapter(Adapter);
        mSpinnerAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_age = mSpinnerAge.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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


