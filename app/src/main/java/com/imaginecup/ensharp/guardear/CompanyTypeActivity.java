package com.imaginecup.ensharp.guardear;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.List;

public class CompanyTypeActivity extends Activity {
    //static final String[] LIST_MENU = {"APPLE", "AUDIO-TEXHNICA", "CRESYN", "LG", "SAMSUNG","SENNHEISER","SHURE",   "SONY", "WESTONE", "XENICS"} ;

    private MobileServiceClient mClient;
    private MobileServiceTable<Company> mCompanyTable;
    private com.imaginecup.ensharp.guardear.SharedPreferences mPref;
    android.content.SharedPreferences setting;
    android.content.SharedPreferences.Editor editor;

    private CompanyTypeAdapter mAdapter;

    //private CompanyType mListView = null;

    private EditText mEtSearch;
    ListView listViewCompany;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_type);
        Log.d("회사명", "CompanyTypeActivity 들어옴");

        mPref = new com.imaginecup.ensharp.guardear.SharedPreferences(this);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        // create a new item
        final Company companyTypeItem = new Company();

        mEtSearch = (EditText)findViewById(R.id.etSearch);
        //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);

        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient("https://guardear.azurewebsites.net", CompanyTypeActivity.this);

            // Get the Mobile Service Table instance to use
            mCompanyTable = mClient.getTable(Company.class);

            mAdapter = new CompanyTypeAdapter(this, R.layout.row_earphone_company);
            listViewCompany = (ListView)findViewById(R.id.listViewCompany);
            listViewCompany.setAdapter(mAdapter);

            Log.d("회사명", "리스트뷰에 셋 어댑터");

            // create a new item
            final Company companyItem = new Company();


            companyItem.setID(mEtSearch.getText().toString());
            companyItem.setComplete(false);

            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {

                    try {
                        // 데이터를 가져오는 리스트
                        final List<Company> result = mCompanyTable.execute().get();


                        Log.d("회사명", "중복확인 result 값 받아오기");
                        Log.d("회사명", "결과값 확인 : " + result.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("회사명", "런 들어옴");

                                if(Looper.myLooper() == null){ Looper.prepare();   }

                                listViewCompany.setAdapter(mAdapter);

                                for(Company item : result){

                                    mAdapter.add(item);
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
            // listViewToDo.OnItemClickListener(new )


        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }


        /*listViewCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                mPref.putValue("earphone_company", item.getCompany().toString(), "userinfo");

                Intent intent = new Intent(getApplicationContext(), EarphoneActivity.class);
                startActivity(intent);

                finish();
            }
        });*/

    }

    public void checkItem(final Company item) {


        mPref.putValue("earphone_company", item.getID().toString(), "userinfo");
        Log.d("이어폰 회사 엑티비티", mPref.getValue("earphone_company", "SAMSUNG", "userinfo"));

        Intent intent = new Intent(getApplicationContext(), EarphoneActivity.class);
        startActivity(intent);

        finish();

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
