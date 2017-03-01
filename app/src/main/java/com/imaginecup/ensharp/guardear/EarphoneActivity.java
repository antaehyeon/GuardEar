package com.imaginecup.ensharp.guardear;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.List;

public class EarphoneActivity extends Activity {

    private MobileServiceClient mClient;
    private MobileServiceTable<Earphone> mEarphoneTable;
    private com.imaginecup.ensharp.guardear.SharedPreferences mPref;

    private EarphoneAdapter mAdapter;

    //private ProgressBar mProgressBar;

    //이어폰 등록 부분
    //private EditText model;
    //private EditText soundpressure;
    //private EditText impedance;
    private EditText mEtSearch;
    ListView listViewToDo;
    Button btn_Next;

    android.content.SharedPreferences setting;
    android.content.SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earphone);

        //이어폰 등록 부분
        //model = (EditText)findViewById(R.id.model);
        //soundpressure = (EditText)findViewById(R.id.soundpressure);
        //impedance = (EditText)findViewById(R.id.impedance);
        mEtSearch = (EditText)findViewById(R.id.etSearch);
        btn_Next = (Button)findViewById(R.id.btn_Next);
        //btn_Next.setVisibility(View.INVISIBLE);

        Log.d("이어폰", "이어폰 onCreate()");

        //mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        // Initialize the progress bar
        //mProgressBar.setVisibility(ProgressBar.GONE);

        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient("https://safeear.azurewebsites.net", EarphoneActivity.this);

            // Get the Mobile Service Table instance to use
            mEarphoneTable = mClient.getTable(Earphone.class);

            mAdapter = new EarphoneAdapter(this, R.layout.row_earphone);
            listViewToDo = (ListView)findViewById(R.id.listViewToDo);
            listViewToDo.setAdapter(mAdapter);

            mPref = new com.imaginecup.ensharp.guardear.SharedPreferences(this);

            setting = getSharedPreferences("setting", 0);
            editor = setting.edit();

            // create a new item
            final Earphone earphoneItem = new Earphone();

            earphoneItem.setID(mEtSearch.getText().toString());
            earphoneItem.setComplete(false);

            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        // 데이터를 가져오는 리스트
                        final List<Earphone> result = mEarphoneTable.execute().get();

                        Log.d("이어폰", "중복확인 result 값 받아오기");
                        Log.d("이어폰", "결과값 확인 : " + result.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("이어폰", "런 들어옴");

                                if(Looper.myLooper() == null){ Looper.prepare();   }

                                listViewToDo.setAdapter(mAdapter);

                                for(Earphone item : result){

                                    mAdapter.add(item);
                                }
                                Looper.loop();
                            }
                        });
                    } catch (final Exception e){
                        //createAndShowDialogFromTask(e, "Error");
                    }
                    return null;
                }
            };
            runAsyncTask(task);


        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }
    }


    // 정보 가져오기
   /* public void getItem(View view){
        Log.d("이어폰", "getItem() 함수 들어옴");

        // create a new item
        final Earphone earphoneItem = new Earphone();

        earphoneItem.setID(mEtSearch.getText().toString());
        earphoneItem.setComplete(false);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // 데이터를 가져오는 리스트
                    final List<Earphone> result = mEarphoneTable.execute().get();

                    Log.d("이어폰", "중복확인 result 값 받아오기");
                    Log.d("이어폰", "결과값 확인 : " + result.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("이어폰", "런 들어옴");

                            if(Looper.myLooper() == null){ Looper.prepare();   }

                            listViewToDo.setAdapter(mAdapter);

                            for(Earphone item : result){

                                mAdapter.add(item);
                            }
                            Looper.loop();
                        }
                    });
                } catch (final Exception e){
                    //createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        runAsyncTask(task);
    }
    */

    // 쿼리하여 완료로 표시되지 않은 모든 항목 반환 ( 바인딩을 위해 항목이 어댑터에 추가됨)
    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = // Get the items that weren't marked as completed and add them in the adapter
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            final MobileServiceList<Earphone> result = mEarphoneTable.where().field("complete").eq(false).execute().get();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.clear();

                                    for (Earphone item : result) {
                                        mAdapter.add(item);
                                    }
                                }
                            });
                        } catch (Exception exception) {
                            //createAndShowDialog(exception, "Error");
                        }
                        return null;
                    }
                }.execute();
    }

    public void checkItem(final Earphone item) {
        String image=null;

        btn_Next.setVisibility(View.VISIBLE);


        Log.d("CheckItem", "checkItem 들어옴");
        Log.d("CheckItem", "정보 들어오나 : " + item.getID().toString());


        mPref.putValue("earphone_company", item.getCompany().toString(), "userinfo");
        mPref.putValue("earphone_model", item.getID().toString(), "userinfo");
        mPref.putValue("earphone_impedance", item.getImpedance().toString(), "userinfo");
        mPref.putValue("earphone_soundpressure", item.getSoundPressure().toString(), "userinfo");

        /*if (item.getID().toString().equals("HSS-100")) {
            image = "hss-100";
        } else if (item.getID().toString().equals("STORMX_BLITZ")) {
            image = "stormaxblitz";
        } else if (item.getID().toString().equals("AirPods")) {
            image = "airpods";
        }else if (item.getID().toString().equals("EarPods")) {
            image = "earpods";
        }else if(item.getID().toString().equals("FIX_XE-501")){
            image = "fixxe_501";
        }else if (item.getID().toString().equals("MDR-EX650AP")) {
            image = "mdr_ex650ap";
        }else if (item.getID().toString().equals("LG_GS200")) {
            image = "lg_gs200";
        }else if (item.getID().toString().equals("EO-IG930BBEGKR")) {
            image = "mo_ig930bbegkr";
        }*/

        if (item.getID().toString().equals("EO-BG920BBKG")) {
            image = "eo_bg920bbkg";
        } else if (item.getID().toString().equals("EO-EG920BWEG")) {
            image = "eo_eg920bweg";
        }else if (item.getID().toString().equals("EO-BN920CFKG")) {
            image = "eo_bn920cfkg";
        } else if (item.getID().toString().equals("EO-BG930CBKGKR")) {
            image = "eo_bg930bkgkr";
        }else if (item.getID().toString().equals("EO-BG935CBKGKR")) {
            image = "eo_bg935cbkgkr";
        }/*else if(item.getID().toString().equals("EO-HS1393WEG")){
            image = "eo_hs1303weg";
        }*/else if (item.getID().toString().equals("EO-IA510BLKGKR")) {
            image = "eo_ia510blkgkr";
        }else if (item.getID().toString().equals("EO-IG930BBEGKR")) {
            image = "eo_ig930bbegkr";
        }else if (item.getID().toString().equals("EO-MN900KWKG")) {
            image = "eo_mn900kwkg";
        }else if (item.getID().toString().equals("EO-MN910VWKG")) {
            image = "eo_mn910vwkg";
        }else if (item.getID().toString().equals("EO-MG920BBKG")) {
            image = "eo_mg920bbkg";
        }
        mPref.putValue("earphone_image", image, "userinfo");

        Log.d("이어폰 정보", item.getCompany().toString());
        Log.d("이어폰 정보", item.getID().toString());
        Log.d("이어폰 정보", item.getImpedance().toString());
        Log.d("이어폰 정보", item.getSoundPressure().toString());
        Log.d("이어폰 정보", image);



        //btn_Next.setVisibility(View.VISIBLE);

        if (mClient == null) {
            return;
        }

        // Set the item as completed and update it in the table

        item.setComplete(true);

        /*AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    checkItemInTable(item);
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
                */
    }

    public void NextClick(View view){

        if(btn_Next.isPressed()) {

            Intent intent = new Intent(getApplicationContext(), AudioMetryActivity.class);
            startActivity(intent);

            finish();

        }
    }

    //이어폰 등록 부분
    /*public void addItem(View view) {

        if (mClient == null) {
            return;
        }
        // Create a new item
        final Earphone item = new Earphone(model.getText().toString(), soundpressure.getText().toString(), impedance.getText().toString());

        Log.d("이어폰 값 확인", model.getText().toString());
        Log.d("이어폰 값 확인", soundpressure.getText().toString());
        Log.d("이어폰 값 확인", impedance.getText().toString());
        Log.d("이어폰 값 확인(item)", item.toString());
        //item.setText(mTextNewToDo.getText().toString());
        item.setComplete(false); // 내부 데이터 초기화


        // Insert the new item
        AsyncTask<Void, Void, Void> task = // Insert the new item
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Log.d("이어폰 Insert the new item", "이어폰 Insert the new item");
                            final Earphone entity = addItemInEarphone(item);

                            //mEarphoneTable.insert(item).get();
                            //if (!item.isComplete()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(!entity.isComplete()) {

                                            mAdapter.add(entity);

                                            Log.d("이어폰", "정보저장 완료");
                                        }
                                    }
                                });
                            //}

                        } catch (final Exception exception) {
                            Log.d("이어폰 Insert the new item", "catch 걸림");

                        }
                        return null;
                    }
                };

        runAsyncTask(task);

        //입력 후 다시 공백칸으로
        model.setText("");
        soundpressure.setText("");
        impedance.setText("");

    }



    public Earphone addItemInEarphone(Earphone item) throws ExecutionException, InterruptedException {
        Earphone entity = mEarphoneTable.insert(item).get();
        return entity;
    }
*/
    /*private class ProgressFilter implements ServiceFilter {
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
    }*/
   private void createAndShowDialogFromTask(final Exception exception, String title) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               createAndShowDialog(exception, "Error");
           }
       });
       Log.d("태그", "createAndShowDialogFromTask");

   }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
        Log.d("태그", "createAndShowDialog1");

    }
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
        Log.d("태그", "createAndShowDialog2");


    }
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        Log.d("태그", "AsyncTask");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();

        }
    }



}