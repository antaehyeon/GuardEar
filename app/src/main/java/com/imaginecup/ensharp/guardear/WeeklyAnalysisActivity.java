package com.imaginecup.ensharp.guardear;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

import java.util.ArrayList;

/**
 * Created by Semin on 2017-02-22.
 */
public class WeeklyAnalysisActivity extends AppCompatActivity implements OnChartValueSelectedListener{

    protected BarChart mChart;
    //private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private IAxisValueFormatter xAxisFormatter;

    public static final int COLOR_NAVY = Color.parseColor("#213b4c");
    public static final int COLOR_SKYBLUE = Color.parseColor("#02ecfb");
    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklyanalysis);
        Intent intent = getIntent();
        String week = intent.getExtras().getString("week");
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.weekly_analysis_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        TextView weeklyanalysisTitleTxt = (TextView) findViewById(R.id.weeklyanalysisTitleTxt);
        TextView weeklyanalysisSubTitleTxt = (TextView) findViewById(R.id.weeklyanalysisSubTitleTxt);
        weeklyanalysisTitleTxt.setText("주간 분석");
        weeklyanalysisSubTitleTxt.setText(week);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("주간 분석");
//        getSupportActionBar().setSubtitle(week);
        chart = (BarChart) findViewById(R.id.chart1);

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

        initiate();
    }

    private void initiate(){
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

        setData(6, 100);

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
            if(i==1){
                yVals1.add(new BarEntry(i, 58));
            } else if(i==2) {
                yVals1.add(new BarEntry(i, 68));
            } else if(i==3) {
                yVals1.add(new BarEntry(i, 78));
            } else if(i==4) {
                yVals1.add(new BarEntry(i, 61));
            }else if(i==5) {
                yVals1.add(new BarEntry(i, 68));
            } else if(i==6) {
                yVals1.add(new BarEntry(i, 95));
            } else if(i==7) {
                yVals1.add(new BarEntry(i, 83));
            }
        }

        MyBarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            Log.i("if","여기 들어옴");
            set1 = (MyBarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            Log.i("else","여기 들어옴");
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
        intent.putExtra("day",xAxisFormatter.getFormattedValue(e.getX(), null));
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
}
