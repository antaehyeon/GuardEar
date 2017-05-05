package com.imaginecup.ensharp.guardear;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semin on 2017-02-27.
 */
public class TimeAnalysisActivity extends Activity {
    private BarChart mChart;
    private TextView dayTxt;


    /* 서버 */
    private MobileServiceClient mClient;
    private MobileServiceTable<ListeningData> mListeningDataTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_timeanalysis);

        mChart = (BarChart) findViewById(R.id.timeChart);
        dayTxt = (TextView) findViewById(R.id.dayTxt);
        Intent intent = getIntent();
        String day = intent.getExtras().getString("day");
        dayTxt.setText(day);
        initiate();


        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient("http://guardear.azurewebsites.net", TimeAnalysisActivity.this);

            // Get the Mobile Service Table instance to use
            mListeningDataTable = mClient.getTable(ListeningData.class);

            // create a new item
            final ListeningData ListeningDataItem = new ListeningData();

            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {

                        // 데이터를 가져오는 리스트
                        final List<ListeningData> result = mListeningDataTable.execute().get();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(Looper.myLooper() == null){ Looper.prepare();   }

                                for(ListeningData item : result){

                                    Log.d("서버로부터 청취정보", "시간 : " + item.getDate());
                                    Log.d("서버로부터 청취정보", "00시 : " + Integer.toString(item.getTime_00()));
                                    Log.d("서버로부터 청취정보", "01시 : " + Integer.toString(item.getTime_01()));
                                    Log.d("서버로부터 청취정보", "02시 : " + Integer.toString(item.getTime_02()));
                                    Log.d("서버로부터 청취정보", "03시 : " + Integer.toString(item.getTime_03()));
                                    Log.d("서버로부터 청취정보", "04시 : " + Integer.toString(item.getTime_04()));
                                    Log.d("서버로부터 청취정보", "05시 : " + Integer.toString(item.getTime_05()));
                                    Log.d("서버로부터 청취정보", "06시 : " + Integer.toString(item.getTime_06()));
                                    Log.d("서버로부터 청취정보", "07시 : " + Integer.toString(item.getTime_07()));
                                    Log.d("서버로부터 청취정보", "08시 : " + Integer.toString(item.getTime_08()));
                                    Log.d("서버로부터 청취정보", "09시 : " + Integer.toString(item.getTime_09()));
                                    Log.d("서버로부터 청취정보", "10시 : " + Integer.toString(item.getTime_10()));
                                    Log.d("서버로부터 청취정보", "11시 : " + Integer.toString(item.getTime_11()));
                                    Log.d("서버로부터 청취정보", "12시 : " + Integer.toString(item.getTime_12()));
                                    Log.d("서버로부터 청취정보", "13시 : " + Integer.toString(item.getTime_13()));
                                    Log.d("서버로부터 청취정보", "14시 : " + Integer.toString(item.getTime_14()));
                                    Log.d("서버로부터 청취정보", "15시 : " + Integer.toString(item.getTime_15()));
                                    Log.d("서버로부터 청취정보", "16시 : " + Integer.toString(item.getTime_16()));
                                    Log.d("서버로부터 청취정보", "17시 : " + Integer.toString(item.getTime_17()));
                                    Log.d("서버로부터 청취정보", "18시 : " + Integer.toString(item.getTime_18()));
                                    Log.d("서버로부터 청취정보", "19시 : " + Integer.toString(item.getTime_19()));
                                    Log.d("서버로부터 청취정보", "20시 : " + Integer.toString(item.getTime_20()));
                                    Log.d("서버로부터 청취정보", "21시 : " + Integer.toString(item.getTime_21()));
                                    Log.d("서버로부터 청취정보", "22시 : " + Integer.toString(item.getTime_22()));
                                    Log.d("서버로부터 청취정보", "23시 : " + Integer.toString(item.getTime_23()));


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

    private void initiate() {
        mChart = (BarChart) findViewById(R.id.timeChart);
        mChart.setPinchZoom(false);
        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately

        //mChart.setDrawValueAboveBar(false);
        //mChart.setDrawValueAboveBar(false);
        mChart.setDrawGridBackground(false);
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);
        mChart.setClipValuesToContent(false);
        mChart.getLegend().setEnabled(false);

        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new TimeAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(24);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextSize(10f);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(6, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setDrawLabels(false);
//        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(6, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

//        Legend l = mChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setFormSize(9f);
//        l.setTextSize(11f);
//        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

//        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart

        setData(23, 100);

        // setting data
//        mSeekBarY.setProgress(50);
//        mSeekBarX.setProgress(12);
        // mChart.setDrawLegend(false);
    }

    private void setData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count + 1; i++) {
//            float mult = (range + 2);
//            float val = (float) (Math.random() * mult);
//
//            if (Math.random() * 100 < 25) {
//                yVals1.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.star)));
//            } else {
//                yVals1.add(new BarEntry(i, val));
//            }
            if (i == 1) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 2) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 3) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 4) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 5) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 6) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 7) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 8) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 9) {
                yVals1.add(new BarEntry(i, 62));
            } else if (i == 10) {
                yVals1.add(new BarEntry(i, 79));
            } else if (i == 11) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 12) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 13) {
                yVals1.add(new BarEntry(i, 50));
            } else if (i == 14) {
                yVals1.add(new BarEntry(i, 62));
            } else if (i == 15) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 16) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 17) {
                yVals1.add(new BarEntry(i, 60));
            } else if (i == 18) {
                yVals1.add(new BarEntry(i, 64));
            } else if (i == 19) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 20) {
                yVals1.add(new BarEntry(i, 87));
            } else if (i == 21) {
                yVals1.add(new BarEntry(i, 72));
            } else if (i == 22) {
                yVals1.add(new BarEntry(i, 0));
            } else if (i == 23) {
                yVals1.add(new BarEntry(i, 50));
            } else if (i == 24) {
                yVals1.add(new BarEntry(i, 0));
            }
        }

        MyBarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            Log.i("if", "여기 들어옴");
            set1 = (MyBarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            Log.i("else", "여기 들어옴");
            set1 = new MyBarDataSet(yVals1, "");

            //set1.setDrawIcons(false);

            //set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setColors(new int[]{this.getResources().getColor(R.color.yellow),
                    this.getResources().getColor(R.color.navy)});
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            set1.setDrawValues(false);

            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(20f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.7f);
            mChart.setData(data);
            mChart.animateY(800);
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
