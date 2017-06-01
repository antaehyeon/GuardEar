package com.imaginecup.ensharp.guardear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class AudioMetryResultActivity extends AppCompatActivity {

    Context mContext;

    // COLORS : 색깔 변수들
    int whiteColor = Color.parseColor("#FFFFFF");
    int baseColor = Color.parseColor("#213b4c");


    // VARIABLE : 변수들
    RelativeLayout relativeLayout;
    TextView title;
    Button toolBarNextButton;

    TextView resultComment;
    TextView resultCondition;

    int resultCount = 0;

    // VARIABLE - GRAPH
    LineChartView resultChart;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape diamond = ValueShape.DIAMOND;
    private ValueShape circle = ValueShape.CIRCLE;
    private ValueShape square = ValueShape.SQUARE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor = false;
    private boolean hasGradientToTransparent = false;

    Line averageLine, leftLine, rightLine;
    LineChartData data;

    List<Line> lines;
    List<PointValue> averageValues, leftValues, rightValues;

    float[] freqData = {250, 500, 1000, 2000, 4000, 6000, 8000};
    float[] freqDecibelAverage;
    float[] tempFreqAverage = {20, 15, 5, 10, 10, 15, 20};
    float[] freqDecibelRight;
    float[] freqDecibelLeft;

    float leftLowFreqAverage, leftMidFreqAverage, leftHighFreqAverage;
    float rightLowFreqAverage, rightMidFreqAverage, rightHighFreqAverage;

    boolean bLeftLowFreq, bLeftMidFreq, bLeftHighFreq;
    boolean bRightLowFreq, bRightMidFreq, bRightHighFreq;

    String comment = "";


    // VARIABLE - SINGLETON
    Singleton mSingleton;


    // FONT SETTING
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    // ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_metry_result);

        // SINGLETON GET INSTANCE
        mSingleton = Singleton.getInstance();

        // ActionBar 대신 ToolBar 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_audioMetryResult);
        toolbar.setTitleTextColor(whiteColor);
        toolbar.setBackgroundColor(baseColor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // Toolbar 에 넣을 txetView(title) 속성 조정
        title = (TextView) toolbar.findViewById(R.id.toolBar_audiometry_title);
        title.setTextColor(whiteColor);
        title.setText("청력측정 결과");
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(20);

        resultComment = (TextView) findViewById(R.id.result_comment);
        resultCondition = (TextView) findViewById(R.id.result_condition);

        // audiometry 최상위 뷰 속성 조정
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_audio_metry_result);
        relativeLayout.setBackgroundColor(baseColor);
        relativeLayout.setPadding(0, 0, 0, 0);

        /* Notification Bar 색상 지정
           https://medium.com/@mindwing/actionbar-%EB%A5%BC-%EB%8B%A4%EB%A4%84%EB%B4%85%EC%8B%9C%EB%8B%A4-401709e5480d#.jsyw89cbu
           http://ghj1001020.tistory.com/m/21 */
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(baseColor);

        // AudioMetryActivity 에서 Data 받아오기 (사용자가 조정한 Decibel을 Singleton 을 이용해서 받아온다)
        freqDecibelLeft = mSingleton.getFreqLeftData();
        freqDecibelRight = mSingleton.getFreqRightData();

        // 왼쪽 데시벨 평균 내는 부분
        leftLowFreqAverage = (freqDecibelLeft[0] + freqDecibelLeft[1]) / 2;
        leftMidFreqAverage = (freqDecibelLeft[2] + freqDecibelLeft[3] + freqDecibelLeft[4]) / 3;
        leftHighFreqAverage = (freqDecibelLeft[5] + freqDecibelLeft[6]) / 2;

        // 오른쪽 데시벨 평균 내는 부분
        rightLowFreqAverage = (freqDecibelRight[0] + freqDecibelRight[1]) / 2;
        rightMidFreqAverage = (freqDecibelRight[2] + freqDecibelRight[3] + freqDecibelRight[4]) / 3;
        rightHighFreqAverage = (freqDecibelRight[5] + freqDecibelRight[6]) / 2;

        comment += "OOO님의 청력측정 결과입니다.\n";

        bLeftLowFreq = judgeFrequency(leftLowFreqAverage);
        bLeftMidFreq = judgeFrequency(leftMidFreqAverage);
        bLeftHighFreq = judgeFrequency(leftHighFreqAverage);

        bRightLowFreq = judgeFrequency(rightLowFreqAverage);
        bRightMidFreq = judgeFrequency(rightMidFreqAverage);
        bRightHighFreq = judgeFrequency(rightHighFreqAverage);

        switch(resultCount) {
            case 0:
            case 1:
            case 2:
                resultCondition.setText("GOOD");
                resultCondition.setTextColor(Color.parseColor("#8BC34A"));
                comment += "현재 청력상태는 전체적으로 양호한 상태입니다.";
                break;
            case 3:
            case 4:
                resultCondition.setText("NORMAL");
                resultCondition.setTextColor(Color.parseColor("#FDD835"));
                comment += "현재 청력상태는 관리가 필요한 상태입니다.";
                break;
            case 5:
            case 6:
                resultCondition.setText("BAD");
                resultCondition.setTextColor(Color.parseColor("#F44336"));
                comment += "현재 청력상태는 좋지 않은 상태입니다.";
                break;
        }

        if (judgeLeftFrequencyForComment()) {
            if (judgeRightFrequencyForComment()) {
                comment += " 어떤 주파수 대역에서도 손실이 의심되지 않습니다.";
            }
        } else if (judgeRightFrequencyForComment()) { }

        resultComment.setText(comment);

        for (int i = 0; i < 7; i++) {
            Log.i("HYEON", "FREQ RIGHT " + i + " : " + freqDecibelRight[i]);
        }
        freqDecibelAverage = new float[7];
        for (int i = 0; i < 7; i++) {
            freqDecibelAverage[i] = (float)(Math.random()*100);
        }
        for (int i = 0; i < 7; i++) {
            Log.i("HYEON", "FREQ AVERAGE " + i + " : " + freqDecibelAverage[i]);
        }

        // Graph Draw
        // 그래프 그리기 - HelloChart
        resultChart = (LineChartView) relativeLayout.findViewById(R.id.resultChart);
        generateData();
        resetViewport();
        resultChart.setViewportCalculationEnabled(false);

        /* 청력측정한 데이터를 액티비티 구성할 때 받아옴
           0, 1, 2, 3, 4, 5, 6, 7
           250, 500, 1000, 2000, 4000, 6000, 8000 */
        mContext = getApplicationContext();

        // ToolBar Button 생성
        toolBarNextButton = (Button) findViewById(R.id.toolBar_audiometry_button);
        toolBarNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AudioMetryResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    } // onCreate VOID


    // Graph Draw - 데이터 생성
    private void generateData() {
        lines = new ArrayList<Line>();

        // 그래프에 데이터 넣는 과정
        averageValues = new ArrayList<PointValue>();
        leftValues = new ArrayList<PointValue>();
        rightValues = new ArrayList<PointValue>();
        for (int i = 0; i < 7; i++) {
//            averageValues.add(new PointValue(freqData[i], freqDecibelAverage[i]));
            leftValues.add(new PointValue(freqData[i], freqDecibelLeft[i]));
            rightValues.add(new PointValue(freqData[i], freqDecibelRight[i]));
        } // FOR INPUT GRAPH DATA 0-6

        for (int i = 0; i < 7; i++) {
            averageValues.add(new PointValue(freqData[i], tempFreqAverage[i]));
        }

        // 라인 속성들 지정
        averageLine = new Line(averageValues);
        leftLine = new Line(leftValues);
        rightLine = new Line(rightValues);
        setGraphProperties(averageLine, 2);
        setGraphProperties(leftLine, 4);
        setGraphProperties(rightLine, 0);

        // ArrayList에 Line 하나 추가
        lines.add(averageLine);
        lines.add(leftLine);
        lines.add(rightLine);

        // LineChartData에 최종으로 ArrayList Lines가 들어감
        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis().setHasLines(true);
            Axis axisY = new Axis().setHasLines(true);

            axisX = Axis.generateAxisFromRange(0.0f, 8000.0f, 1000.0f);

            axisX.setName("Frequency(Hz)");
            axisY.setName("DBHL(Decibels)");

            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        resultChart.setLineChartData(data);
    } // GENERATE DATA FUNCTION

    // Graph Draw - 그래프 범위 조정
    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(resultChart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = 8000;
        resultChart.setMaximumViewport(v);
        resultChart.setCurrentViewport(v);
    } // RESET VIEW PORT FUNCTION

    public void setGraphProperties(Line mLine, int color) {
        mLine.setColor(ChartUtils.COLORS[color]);
        mLine.setShape(diamond);
        mLine.setCubic(isCubic);
        mLine.setFilled(isFilled);
        mLine.setHasLabels(hasLabels);
        mLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
        mLine.setHasLines(hasLines);
        mLine.setHasPoints(hasPoints);
        mLine.setPointRadius(1);
    }

    public boolean judgeFrequency(float data) {
        if (data >= 20) {
            resultCount ++;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean judgeLeftFrequencyForComment() {
        if (!bLeftLowFreq && !bLeftMidFreq && !bLeftHighFreq) {
            return true;
        } else {
            comment += " 왼쪽 [";
        }

        if (bLeftLowFreq) {
            bLeftLowFreq = false;
            comment += "저 주파수 ";
        }
        if (bLeftMidFreq) {
            bLeftMidFreq = false;
            comment += "중간 주파수 ";
        }
        if (bLeftHighFreq) {
            bLeftHighFreq = false;
            comment += "고 주파수 ";
        }

        comment += "] ";

        return false;
    }

    public boolean judgeRightFrequencyForComment() {
        if (!bRightLowFreq && !bRightMidFreq && !bRightHighFreq) {
            return true;
        } else {
            comment += " 오른쪽 [";
        }

        if (bRightLowFreq) {
            bRightLowFreq = false;
            comment += "저 주파수 ";
        }
        if (bRightMidFreq) {
            bRightMidFreq = false;
            comment += "중간 주파수 ";
        }
        if (bRightHighFreq) {
            bRightHighFreq = false;
            comment += "고 주파수 ";
        }

        comment += "] ";
        comment += "부분에서 약간의 청력손실이 의심되니 병원의 진료를 권합니다.";

        return false;
    }


} // AudioMetryResultActivity CLASS