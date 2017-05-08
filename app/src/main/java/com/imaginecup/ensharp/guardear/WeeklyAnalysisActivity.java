package com.imaginecup.ensharp.guardear;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semin on 2017-02-22.
 */
public class WeeklyAnalysisActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    protected BarChart mChart;
    //private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private IAxisValueFormatter xAxisFormatter;
    private String mKeyName;
    public static final int COLOR_NAVY = Color.parseColor("#213b4c");
    public static final int COLOR_SKYBLUE = Color.parseColor("#02ecfb");
    BarChart chart;
    ArrayList<BarEntry> BARENTRY;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    BarData BARDATA;
    private LinearLayout dailyDecibelBar;
    private LinearLayout dailyTimeBar;
    private TextView averageDecibelTxt;
    private TextView averageTimeTxt;
    private TextView detailAverageDecibelTxt;
    private TextView detailAverageTimeTxt;
    private int averageDecibel;
    private int averageTime;

    /* 서버 */
    private MobileServiceClient mClient;
    private MobileServiceTable<DailyData> mDailyDataTable;
    private List<DailyData> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklyanalysis);
        Intent intent = getIntent();
        String week = intent.getExtras().getString("week");
        String year = intent.getExtras().getString("year");
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.weekly_analysis_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        TextView weeklyanalysisTitleTxt = (TextView) findViewById(R.id.weeklyanalysisTitleTxt);
        TextView weeklyanalysisSubTitleTxt = (TextView) findViewById(R.id.weeklyanalysisSubTitleTxt);
        weeklyanalysisTitleTxt.setText("주간 분석");
        weeklyanalysisSubTitleTxt.setText(week);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dailyDecibelBar = (LinearLayout) findViewById(R.id.dailyDecibelBar);
        dailyTimeBar = (LinearLayout) findViewById(R.id.dailyTimeBar);
        averageDecibelTxt = (TextView) findViewById(R.id.averageDecibelTxt);
        averageTimeTxt = (TextView) findViewById(R.id.averageTimeTxt);
        detailAverageDecibelTxt = (TextView) findViewById(R.id.detailAverageDecibelTxt);
        detailAverageTimeTxt = (TextView) findViewById(R.id.detailAverageTimeTxt);
        averageDecibel = 0;
        averageTime = 0;
//        getSupportActionBar().setTitle("주간 분석");
//        getSupportActionBar().setSubtitle(week);
        chart = (BarChart) findViewById(R.id.chart1);
        String parsingWeek = week.split("~")[0];
        String month = parsingWeek.split("월")[0];
        String day = parsingWeek.split("월")[1].trim().split("일")[0];
        if(Integer.parseInt(day)<10){
            day = "0"+day;
        }
        mKeyName = year + "_" + month + "_" + day;
        Log.d("수정한 날짜", mKeyName);
        // 일별 정보 가져오는 횟수 세기

        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient("http://guardear.azurewebsites.net", WeeklyAnalysisActivity.this);

            // Get the Mobile Service Table instance to use
            mDailyDataTable = mClient.getTable(DailyData.class);

            // create a new item
            final ListeningData ListeningDataItem = new ListeningData();

            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        // 데이터를 가져오는 리스트
                        //final List<DailyData> result = mDailyDataTable.where().field("date").ge(mKeyName).orderBy("date", QueryOrder.Ascending).top(7).execute().get();
                        result = mDailyDataTable.where().field("date").ge(mKeyName).orderBy("date", QueryOrder.Ascending).top(7).execute().get();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                }
                                for (DailyData item : result) {
                                    averageDecibel += item.getAvg_dB();
                                    averageTime += item.getAvg_time();
//                                    Log.d("서버로부터 청취정보", "시간 : " + item.getDate());
//                                    Log.d("서버로부터 청취정보", "dB : " + Integer.toString(item.getAvg_dB()));
//                                    Log.d("서버로부터 청취정보", "time : " + Integer.toString(item.getAvg_time()));
                                }
                                averageDecibel = averageDecibel / 7;
                                averageTime = averageTime / 7;
                                Log.d("평균데시벨", "" + averageDecibel);
                                Log.d("평균시간", "" + averageTime);
                                initiate();
                                Looper.loop();
                            }
                        });
                    } catch (final Exception e) {
                        //createAndShowDialogFromTask(e, "Error");
                    }
                    return null;
                }
            };
            runAsyncTask(task);


        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }


//        BARENTRY = new ArrayList<>();
//
//        BarEntryLabels = new ArrayList<String>();
//
//        AddValuesToBARENTRY();
//
//        AddValuesToBarEntryLabels();
//
//        Bardataset = new BarDataSet(BARENTRY, "Projects");
//
//        BARDATA = new BarData(BarEntryLabels, Bardataset);
//
//        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        chart.setData(BARDATA);
//
//        chart.animateY(3000);
        //XAxis XAxis = mChart.getXAxis ();

        //initiate();
    }

    private void initiate() {
        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
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

        xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(6);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(7, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTfLight);
        //rightAxis.setLabelCount(0, false);
//        rightAxis.setValueFormatter(custom);
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

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

        ViewGroup.LayoutParams dailyDecibelParams = dailyDecibelBar.getLayoutParams();
        ViewGroup.LayoutParams dailyTimeParams = dailyTimeBar.getLayoutParams();
        double ratio = averageDecibel / 120.0;
        dailyDecibelParams.width = (int) ((double) 1258 * ratio);
        dailyDecibelBar.setLayoutParams(dailyDecibelParams);
        ratio = averageTime / 90.0;
        dailyTimeParams.width = (int) ((double) 1258 * ratio);
        dailyTimeBar.setLayoutParams(dailyTimeParams);
        averageDecibelTxt.setText(averageDecibel + "");
        averageTimeTxt.setText(timeFormat(averageTime));
        detailAverageDecibelTxt.setText(averageDecibel + "dB");
        detailAverageTimeTxt.setText(timeFormat(averageTime));
        setData(6, 100);
        // setting data
//        mSeekBarY.setProgress(50);
//        mSeekBarX.setProgress(12);
        // mChart.setDrawLegend(false);
    }

    private String timeFormat(int time) {
        if (time < 60) {
            return time + "분";
        } else {
            int hour = time / 60;
            int minute = time % 60;
            return hour + "시간 " + minute + "분";
        }
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
            if (i == 1) { // 일
                //yVals1.add(new BarEntry(i, 58));
                yVals1.add(new BarEntry(i, result.get(0).getAvg_dB()));
                Log.d("데시벨 정보", "dB : " + Integer.toString(result.get(0).getAvg_dB()));
            } else if (i == 2) {
                yVals1.add(new BarEntry(i, result.get(1).getAvg_dB()));
                Log.d("데시벨 정보", "dB : " + Integer.toString(result.get(1).getAvg_dB()));
            } else if (i == 3) {
                yVals1.add(new BarEntry(i, result.get(2).getAvg_dB()));
                Log.d("데시벨 정보", "dB : " + Integer.toString(result.get(2).getAvg_dB()));
            } else if (i == 4) {
                yVals1.add(new BarEntry(i, result.get(3).getAvg_dB()));
                Log.d("데시벨 정보", "dB : " + Integer.toString(result.get(3).getAvg_dB()));
            } else if (i == 5) {
                yVals1.add(new BarEntry(i, result.get(4).getAvg_dB()));
                Log.d("데시벨 정보", "dB : " + Integer.toString(result.get(4).getAvg_dB()));
            } else if (i == 6) {
                yVals1.add(new BarEntry(i, result.get(5).getAvg_dB()));
                Log.d("데시벨 정보", "dB : " + Integer.toString(result.get(5).getAvg_dB()));
            } else if (i == 7) {
                yVals1.add(new BarEntry(i, result.get(6).getAvg_dB()));
                Log.d("데시벨 정보", "dB : " + Integer.toString(result.get(6).getAvg_dB()));
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
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.5f);
            mChart.setData(data);
            mChart.animateY(800);
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        Intent intent = new Intent(this, TimeAnalysisActivity.class);
        intent.putExtra("day", xAxisFormatter.getFormattedValue(e.getX(), null));
        //Log.i("요일값",xAxisFormatter.getFormattedValue(e.getX(), null));
        startActivity(intent);

//        RectF bounds = mOnValueSelectedRectF;
//        mChart.getBarBounds((BarEntry) e, bounds);
//        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);
//
//        Log.i("bounds", bounds.toString());
//        Log.i("position", position.toString());
//
//        Log.i("x-index",
//                "low: " + mChart.getLowestVisibleX() + ", high: "
//                        + mChart.getHighestVisibleX());
//
//        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
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
        if (exception.getCause() != null) {
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
