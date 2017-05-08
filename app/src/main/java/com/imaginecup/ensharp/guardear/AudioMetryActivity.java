package com.imaginecup.ensharp.guardear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class AudioMetryActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    TextView title;

    int whiteColor = Color.parseColor("#FFFFFF");
    int baseColor = Color.parseColor("#213b4c");
    int pointColor = Color.parseColor("#2f4959");

    LineChartView chart;
    private LineChartData data;


    // 그래프 V2 변수들
    int maxNumberOfLines = 4;
    int numberOfPoints = 8;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape diamond = ValueShape.DIAMOND;
    private ValueShape circle = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;

    TextView txtStep, txtFreq, txtEar;

    Button btnCantHear;
    Button btnFreqUp;
    Button btnFreqDown;

    // Tone Generator
    private int duration = 60; // seconds
    private int sampleRate = 44100;
    private int numSamples = duration * sampleRate;
    private double sample[] = new double[numSamples];
    private double freqOfTone = 1000; // hz

    public byte generatedSnd[] = new byte[2 * numSamples];

    private AudioManager audioManager;
    public AudioTrack audioTrack = null;

    boolean btnCantHearClick = false;

    public static final int LEFT = 2;
    public static final int RIGHT = 1;

    public static final int SHORT = 1;
    public static final int LONG = 2;

    public static final int UP = 1;
    public static final int DOWN = 2;

    List<Line> lines;
    List<PointValue> leftValue, rightValue;

    Line leftLine, rightLine;

    float leftTone = 0.08f;
    float rightTone = 0.08f;

    // Frequency Index (RightValue, LeftValue)
    public static final int FREQ250 = 0;
    public static final int FREQ500 = 1;
    public static final int FREQ1000 = 2;
    public static final int FREQ2000 = 3;
    public static final int FREQ4000 = 4;
    public static final int FREQ6000 = 5;
    public static final int FREQ8000 = 6;

    // Freq Left Decibel
    int freq250LeftDecibel = 40;
    int freq500LeftDecibel = 40;
    int freq1000LeftDecibel = 40;
    int freq2000LeftDecibel = 40;
    int freq4000LeftDecibel = 40;
    int freq6000LeftDecibel = 40;
    int freq8000LeftDecibel = 40;

    // Freq Right Decibel
    int freq250RightDecibel = 40;
    int freq500RightDecibel = 40;
    int freq1000RightDecibel = 40;
    int freq2000RightDecibel = 40;
    int freq4000RightDecibel = 40;
    int freq6000RightDecibel = 40;
    int freq8000RightDecibel = 40;

    int step = 1;
    int earState = RIGHT;

    private TimerTask second;
    private final Handler handler = new Handler();

    int upDownCount = 8;

    Singleton mSingleton;

    Button tempButton;

    boolean audioMetryEnd = false;

    Button toolbarNextButton;

    boolean isRepeatAudioTrackPlayAndStop = false;

    public void decibelStart() {
        second = new TimerTask() {
            @Override
            public void run() {
                Update();
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 300);
    }

    public void Update() {
        Runnable updater = new Runnable() {
            public void run() {

                controlFreq(step);
                genTone();
                playSound(leftTone, rightTone);
            }
        };
        handler.post(updater);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_metry);

        // ActionBar 대신 ToolBar 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar_audiometry);
        toolbar.setTitleTextColor(whiteColor);
        toolbar.setBackgroundColor(baseColor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // Toolbar 에 넣을 txetView(title) 속성 조정
        title = (TextView) toolbar.findViewById(R.id.toolBar_audiometry_title);
        title.setTextColor(whiteColor);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(20);

        // audiometry 최상위 뷰 속성 조정
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_audio_metry);
        relativeLayout.setBackgroundColor(baseColor);
        relativeLayout.setPadding(0, 0, 0, 0);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                numSamples, AudioTrack.MODE_STATIC);

        // Notification Bar 색상 지정
        //https://medium.com/@mindwing/actionbar-%EB%A5%BC-%EB%8B%A4%EB%A4%84%EB%B4%85%EC%8B%9C%EB%8B%A4-401709e5480d#.jsyw89cbu
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        //http://ghj1001020.tistory.com/m/21
        window.setStatusBarColor(baseColor);


        // 그래프 그리기 - HelloChart
        chart = (LineChartView) relativeLayout.findViewById(R.id.chart);

        // 그래프에 랜덤 데이터 넣는 부분이였지만, 초기데이터 40으로 지정함
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = 40;
            }
        }

        generateData();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);

        resetViewport();

        // 아래쪽에서 STEP 변수들
        txtStep = (TextView) findViewById(R.id.tv_step);
        txtFreq = (TextView) findViewById(R.id.tv_freq);

        txtEar = (TextView) findViewById(R.id.tv_ear);
        // BLUE : #2196F3 (BLUE)
        // RED : #F44336 (RED)
        txtEar.setTextColor(Color.parseColor("#F44336"));

        btnCantHear = (Button) findViewById(R.id.btn_cantHear);
        btnCantHear.setTextColor(pointColor);
        btnCantHear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (audioMetryEnd) {
                    mSingleton.setFreqRightData(
                            freq250LeftDecibel,
                            freq500LeftDecibel,
                            freq1000LeftDecibel,
                            freq2000LeftDecibel,
                            freq4000LeftDecibel,
                            freq6000LeftDecibel,
                            freq8000LeftDecibel);

                    second.cancel();
                    second = null;

                    Intent intent = new Intent(AudioMetryActivity.this, AudioMetryResultActivity.class);
                    startActivity(intent);
                    finish();
                    return ;
                }

                if (!btnCantHearClick) {
                    btnFreqDown.setEnabled(true);
                    btnFreqUp.setEnabled(true);
                    btnCantHear.setTextSize(16);
                    btnCantHear.setTypeface(null, Typeface.BOLD);
                    btnCantHear.setText("최대한 들을 수 있는 곳에서 클릭하세요");
                    btnCantHearClick = true;
                    if (earState == RIGHT) {
                        toastMessage("오른쪽 귀 테스트가 시작됩니다", SHORT);
                    } else {
                        toastMessage("왼쪽 귀 테스트가 시작됩니다", SHORT);
                    }

                    toastMessage("버튼을 이용해 들리지 않을때까지 조정하세요", LONG);
                    txtEar.setTextSize(20);
                    if (earState == RIGHT) {
                        audioMetryDataChange(1, 1000, RIGHT);
                        leftTone = 0;
                    } else {
                        audioMetryDataChange(1, 1000, LEFT);
                        leftTone = 0.08f;
                        rightTone = 0;
                    }

                    decibelStart();
                } else {
                    // 역치 정하고 데이터 저장하는 부분으로 진행
                    controlDecibelWithHardware();
                    hardWareVolumeControl(11);
                    upDownCount = 8;
                    step++;

                    if (earState == RIGHT && step == 8) {
                        second.cancel();
                        step = 1;
                        earState = LEFT;
                        btnFreqDown.setEnabled(false);
                        btnFreqUp.setEnabled(false);
                        toastMessage("왼쪽 테스트를 시작하려면 버튼을 눌러주세요", LONG);
                        btnCantHear.setText("왼쪽 테스트를 \n시작하려면 클릭하세요");
                        btnCantHear.setTextSize(16);
                        btnCantHear.setTypeface(null, Typeface.BOLD);
                        btnCantHearClick = false;
                        return;
                    }

                    second.cancel();
                    second = null;
                    int tempFreq = stepToFreq(step);
                    audioMetryDataChange(step, tempFreq, earState);
                    controlFreq(step);
                    genTone();
                    decibelStart();
                }

                if (earState == LEFT && step == 7) {
                    toastMessage("마지막 단계입니다", LONG);
                    btnCantHear.setText("클릭하시면\n결과가 저장됩니다");
                    btnCantHear.setTextSize(16);
                    btnCantHear.setTypeface(null, Typeface.BOLD);
                    audioMetryEnd = true;

                    return ;
                }

                // 다른 Class로 넘겨주기 위해서 마지막 단계일경우 데시벨 정보를 저장시킴
                if (step == 1 && earState == LEFT) {
                    mSingleton.setFreqRightData(
                            freq250RightDecibel,
                            freq500RightDecibel,
                            freq1000RightDecibel,
                            freq2000RightDecibel,
                            freq4000RightDecibel,
                            freq6000RightDecibel,
                            freq8000RightDecibel);
                }
            }
        });

        btnFreqDown = (Button) findViewById(R.id.btn_freqDown);
        btnFreqDown.setTextColor(pointColor);
        btnFreqDown.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean decibelZero = judgmentDecibel(step);
                if (decibelZero) {
                    upDownCount = 0;
                    controlDecibelWithHardware();
                    return;
                }

                controlDecibel(step, DOWN);
                upDownCount --;
                controlDecibelWithHardware();
                controlGraphY(step, earState);
            }
        });

        btnFreqUp = (Button) findViewById(R.id.btn_freqUp);
        btnFreqUp.setTextColor(pointColor);
        btnFreqUp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlDecibel(step, UP);
                upDownCount ++;
                controlDecibelWithHardware();
                controlGraphY(step, earState);
            }
        });

        btnCantHear.setText("시작하려면 버튼을 \n클릭하세요");
        btnCantHear.setTextSize(16);
        btnCantHear.setTypeface(null, Typeface.BOLD);

        // 하드웨어 오디오 볼륨 조절
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        hardWareVolumeControl(11);

        txtStep.setText(null);
        txtFreq.setText(null);
        txtEar.setText(null);

        tempButton = (Button) findViewById(R.id.btn_tempNextActivity);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setRandomFrequency();

                Intent intent = new Intent(AudioMetryActivity.this, AudioMetryResultActivity.class);
                startActivity(intent);
            }
        });

        // SINGLETON GET INSTANCE
        mSingleton = Singleton.getInstance();

        // BUTTON (FREQ 조절) 비활성화
        btnFreqDown.setEnabled(false);
        btnFreqUp.setEnabled(false);

        toolbarNextButton = (Button) findViewById(R.id.toolBar_audiometry_button);
        toolbarNextButton.setVisibility(View.INVISIBLE);
    }

    /*
        seque   0,   1,    2,    3,    4,    5,    6
        index 250, 500, 1000, 2000, 4000, 6000, 8000
    */
    public void setGraphData(int index, int graphFreq, int graphDecibel, int graphEar) {
        PointValue tempPV = new PointValue(graphFreq, graphDecibel);

        // LEFT == 1
        if (graphEar == LEFT) {
            leftValue.set(index, tempPV);
        } else {
            rightValue.set(index, tempPV);
        }

        chart.invalidate();
    }

    public void setSineWaveData(int duration, int sampleRate, int freqOfTone) {
        sample = null;
        this.duration = duration;
        this.sampleRate = sampleRate;
        this.freqOfTone = freqOfTone;

        numSamples = duration * sampleRate;
        sample = new double[numSamples];

        generatedSnd = new byte[2 * numSamples];
    }

    public void toastMessage(CharSequence text, int mode) {
        int toastMode;
        Context context = getApplicationContext();
        if (mode == 1) {
            toastMode = Toast.LENGTH_SHORT;
        } else {
            toastMode = Toast.LENGTH_LONG;
        }

        Toast toast = Toast.makeText(context, text, toastMode);
        toast.show();
    }

    public void audioMetryDataChange(int step, int freq, int ear) {
        if (step < 10) {
            txtStep.setText("0" + String.valueOf(step));
        } else {
            txtStep.setText(String.valueOf(step));
        }
        txtFreq.setText(String.valueOf(freq));
        if (ear == 1) {
            txtEar.setText("RIGHT");
        } else {
            txtEar.setText("LEFT");
        }
    }

    public void controlGraphY(int step, int ear) {
        switch (step) {
            case 1:
                // 1단계일경우
                if (ear == LEFT) {
                    setGraphData(FREQ1000, 1000, freq1000LeftDecibel, ear);
                } else {
                    setGraphData(FREQ1000, 1000, freq1000RightDecibel, ear);
                }
                break;
            case 2:
                if (ear == LEFT) {
                    setGraphData(FREQ250, 250, freq250LeftDecibel, ear);
                } else {
                    setGraphData(FREQ250, 250, freq250RightDecibel, ear);
                }
                break;
            case 3:
                if (ear == LEFT) {
                    setGraphData(FREQ500, 500, freq500LeftDecibel, ear);
                } else {
                    setGraphData(FREQ500, 500, freq500RightDecibel, ear);
                }
                break;
            case 4:
                if (ear == LEFT) {
                    setGraphData(FREQ2000, 2000, freq2000LeftDecibel, ear);
                } else {
                    setGraphData(FREQ2000, 2000, freq2000RightDecibel, ear);
                }
                break;
            case 5:
                if (ear == LEFT) {
                    setGraphData(FREQ4000, 4000, freq4000LeftDecibel, ear);
                } else {
                    setGraphData(FREQ4000, 4000, freq4000RightDecibel, ear);
                }
                break;
            case 6:
                if (ear == LEFT) {
                    setGraphData(FREQ6000, 6000, freq6000LeftDecibel, ear);
                } else {
                    setGraphData(FREQ6000, 6000, freq6000RightDecibel, ear);
                }
                break;
            case 7:
                if (ear == LEFT) {
                    setGraphData(FREQ8000, 8000, freq8000LeftDecibel, ear);
                } else {
                    setGraphData(FREQ8000, 8000, freq8000RightDecibel, ear);
                }
                break;
        }
    }

    public void controlFreq(int step) {
        switch (step) {
            case 1:
                setSineWaveData(2, 44100, 1000);
                break;
            case 2:
                setSineWaveData(2, 44100, 250);
                break;
            case 3:
                setSineWaveData(2, 44100, 500);
                break;
            case 4:
                setSineWaveData(2, 44100, 2000);
                break;
            case 5:
                setSineWaveData(2, 44100, 4000);
                break;
            case 6:
                setSineWaveData(2, 44100, 6000);
                break;
            case 7:
                setSineWaveData(2, 44100, 8000);
                break;
        }
    }

    public int stepToFreq(int step) {
        switch (step) {
            case 1:
                return 1000;
            case 2:
                return 250;
            case 3:
                return 500;
            case 4:
                return 2000;
            case 5:
                return 4000;
            case 6:
                return 6000;
            case 7:
                return 8000;
        }
        return 0;
    }

    // 하드웨어의 볼륨을 조절해 데시벨을 조정한다
    public void controlDecibelWithHardware() {
        switch (upDownCount) {
            case 10:
                hardWareVolumeControl(15);
                break;
            case 9:
                hardWareVolumeControl(13);
                break;
            case 8: // 40데시벨
                hardWareVolumeControl(11);
                break;
            case 7: // 35데시벨
                hardWareVolumeControl(9);
                break;
            case 6: // 30데시벨
                hardWareVolumeControl(7);
                break;
            case 5: // 25데시벨
                hardWareVolumeControl(5);
                break;
            case 4: // 20데시벨
                hardWareVolumeControl(3);
                if (earState == RIGHT) {
                    playTone(0, 0.06f);
                } else {
                    playTone(0.06f, 0);
                }
                break;
            case 3: // 15데시벨
                hardWareVolumeControl(2);
                if (earState == RIGHT) {
                    playTone(0, 0.05f);
                } else {
                    playTone(0.05f, 0);
                }
                break;
            case 2: // 10데시벨
                hardWareVolumeControl(1);
                if (earState == RIGHT) {
                    playTone(0, 0.015f);
                } else {
                    playTone(0.015f, 0);
                }
                break;
            case 1: // 5데시벨
                hardWareVolumeControl(1);
                if (earState == RIGHT) {
                    playTone(0, 0.007f);
                } else {
                    playTone(0.007f, 0);
                }
                break;
            case 0: // 0데시벨
                hardWareVolumeControl(0);
                break;
            default:
                hardWareVolumeControl(15);
                break;
        }
    }

    public boolean judgmentDecibel(int step) {
        int currentFreq = stepToFreq(step);

        if (earState == LEFT) {
            switch (currentFreq) {
                case 250:
                    if (freq250LeftDecibel == 0) return true;
                case 500:
                    if (freq500LeftDecibel == 0) return true;
                case 1000:
                    if (freq1000LeftDecibel == 0) return true;
                case 2000:
                    if (freq2000LeftDecibel == 0) return true;
                case 4000:
                    if (freq4000LeftDecibel == 0) return true;
                case 6000:
                    if (freq6000LeftDecibel == 0) return true;
                case 8000:
                    if (freq8000LeftDecibel == 0) return true;
            }
        }

        if (earState == RIGHT) {
            switch (currentFreq) {
                case 250:
                    if (freq250RightDecibel == 0) return true;
                case 500:
                    if (freq500RightDecibel == 0) return true;
                case 1000:
                    if (freq1000RightDecibel == 0) return true;
                case 2000:
                    if (freq2000RightDecibel == 0) return true;
                case 4000:
                    if (freq4000RightDecibel == 0) return true;
                case 6000:
                    if (freq6000RightDecibel == 0) return true;
                case 8000:
                    if (freq8000RightDecibel == 0) return true;
            }
        }
        return false;
    }

    public void controlDecibel(int step, int mode) {
        switch (step) {
            case 1:
                if (earState == LEFT) {
                    if (mode == UP) {
                        freq1000LeftDecibel += 5;
                    } else {
                        freq1000LeftDecibel -= 5;
                    }
                } else {
                    if (mode == UP) {
                        freq1000RightDecibel += 5;
                    } else {
                        freq1000RightDecibel -= 5;
                    }
                }
                break;
            case 2:
                if (earState == LEFT) {
                    if (mode == UP) {
                        freq250LeftDecibel += 5;
                    } else {
                        freq250LeftDecibel -= 5;
                    }
                } else {
                    if (mode == UP) {
                        freq250RightDecibel += 5;
                    } else {
                        freq250RightDecibel -= 5;
                    }
                }
                break;
            case 3:
                if (earState == LEFT) {
                    if (mode == UP) {
                        freq500LeftDecibel += 5;
                    } else {
                        freq500LeftDecibel -= 5;
                    }
                } else {
                    if (mode == UP) {
                        freq500RightDecibel += 5;
                    } else {
                        freq500RightDecibel -= 5;
                    }
                }
                break;
            case 4:
                if (earState == LEFT) {
                    if (mode == UP) {
                        freq2000LeftDecibel += 5;
                    } else {
                        freq2000LeftDecibel -= 5;
                    }
                } else {
                    if (mode == UP) {
                        freq2000RightDecibel += 5;
                    } else {
                        freq2000RightDecibel -= 5;
                    }
                }
                break;
            case 5:
                if (earState == LEFT) {
                    if (mode == UP) {
                        freq4000LeftDecibel += 5;
                    } else {
                        freq4000LeftDecibel -= 5;
                    }
                } else {
                    if (mode == UP) {
                        freq4000RightDecibel += 5;
                    } else {
                        freq4000RightDecibel -= 5;
                    }
                }
                break;
            case 6:
                if (earState == LEFT) {
                    if (mode == UP) {
                        freq6000LeftDecibel += 5;
                    } else {
                        freq6000LeftDecibel -= 5;
                    }
                } else {
                    if (mode == UP) {
                        freq6000RightDecibel += 5;
                    } else {
                        freq6000RightDecibel -= 5;
                    }
                }
                break;
            case 7:
                if (earState == LEFT) {
                    if (mode == UP) {
                        freq8000LeftDecibel += 5;
                    } else {
                        freq8000LeftDecibel -= 5;
                    }
                } else {
                    if (mode == UP) {
                        freq8000RightDecibel += 5;
                    } else {
                        freq8000RightDecibel -= 5;
                    }
                }
                break;
        }
    }

    // 그래프 V2
    private void generateData() {

        lines = new ArrayList<Line>();

        // 그래프에 데이터 넣는 과정
        leftValue = new ArrayList<PointValue>();
        rightValue = new ArrayList<PointValue>();

        // PointValue (X, Y좌표)
        leftValue.add(new PointValue(250, 40));
        leftValue.add(new PointValue(500, 40));
        leftValue.add(new PointValue(1000, 40));
        leftValue.add(new PointValue(2000, 40));
        leftValue.add(new PointValue(4000, 40));
        leftValue.add(new PointValue(6000, 40));
        leftValue.add(new PointValue(8000, 40));

        // PointValue (X, Y좌표)
        rightValue.add(new PointValue(250, 40));
        rightValue.add(new PointValue(500, 40));
        rightValue.add(new PointValue(1000, 40));
        rightValue.add(new PointValue(2000, 40));
        rightValue.add(new PointValue(4000, 40));
        rightValue.add(new PointValue(6000, 40));
        rightValue.add(new PointValue(8000, 40));

        leftLine = new Line(leftValue);
        rightLine = new Line(rightValue);

        leftLine.setColor(ChartUtils.COLORS[4]); // RED
        rightLine.setColor(ChartUtils.COLORS[0]); // BLUE

        leftLine.setShape(circle);
        rightLine.setShape(diamond);

        leftLine.setCubic(isCubic);
        leftLine.setFilled(isFilled);
        leftLine.setHasLabels(hasLabels);
        leftLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
        leftLine.setHasLines(hasLines);
        leftLine.setHasPoints(hasPoints);

        rightLine.setCubic(isCubic);
        rightLine.setFilled(isFilled);
        rightLine.setHasLabels(hasLabels);
        rightLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
        rightLine.setHasLines(hasLines);
        rightLine.setHasPoints(hasPoints);

        leftLine.setPointRadius(5);
        rightLine.setPointRadius(5);

        lines.add(leftLine);
        lines.add(rightLine);

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis().setHasLines(true);

            List<Float> xData = new ArrayList<Float>();
            xData.add(0f);
            xData.add(250f);
            xData.add(500f);
            xData.add(1000f);
            xData.add(2000f);
            xData.add(4000f);

            xData.add(6000f);
            xData.add(8000f);

            axisX = Axis.generateAxisFromRange(0.0f, 8000.0f, 1000.0f);
            axisX.setHasLines(true);

            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Frequency(Hz)");
                axisY.setName("DBHL(Decibels)");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        v.right = 8000;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    void genTone() {
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    public void playSound(float left, float right) {
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.getChannelConfiguration();
        audioTrack.setStereoVolume(left, right);

        if (isRepeatAudioTrackPlayAndStop) {
            audioTrack.stop();
            isRepeatAudioTrackPlayAndStop = false;
        } else {
            audioTrack.play();
            isRepeatAudioTrackPlayAndStop = true;
        }
    }

    public void playTone(float left, float right) { // overloaded playTone
        // method with volume
        // determined by user
        Float leftVolume = left;
        Float rightVolume = right;
        try {
            audioTrack.write(generatedSnd, 0, generatedSnd.length);// pass
            // genSound
            // into
            // write
            // method
            audioTrack.setStereoVolume(leftVolume, rightVolume); // left and
            // right
            // are set
            // by
            // user
            // choice
//            audioTrack.play();
        } catch (Exception e) { // error message if not playable
            Toast.makeText(getApplicationContext(), "Error playing audio",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void hardWareVolumeControl(int volume) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    public void setRandomFrequency() {
        Random random = new Random();
        freq250LeftDecibel = random.nextInt(41);
        freq250RightDecibel = random.nextInt(41);
        freq500LeftDecibel = random.nextInt(41);
        freq500RightDecibel = random.nextInt(41);
        freq1000LeftDecibel = random.nextInt(41);
        freq1000RightDecibel = random.nextInt(41);
        freq2000LeftDecibel = random.nextInt(41);
        freq2000RightDecibel = random.nextInt(41);
        freq4000LeftDecibel = random.nextInt(41);
        freq4000RightDecibel = random.nextInt(41);
        freq6000LeftDecibel = random.nextInt(41);
        freq6000RightDecibel = random.nextInt(41);
        freq8000LeftDecibel = random.nextInt(41);
        freq8000RightDecibel = random.nextInt(41);

        mSingleton.setFreqRightData(
                freq250LeftDecibel,
                freq500LeftDecibel,
                freq1000LeftDecibel,
                freq2000LeftDecibel,
                freq4000LeftDecibel,
                freq6000LeftDecibel,
                freq8000LeftDecibel);

        mSingleton.setFreqRightData(
                freq250RightDecibel,
                freq500RightDecibel,
                freq1000RightDecibel,
                freq2000RightDecibel,
                freq4000RightDecibel,
                freq6000RightDecibel,
                freq8000RightDecibel);
    }

    private class GeneratedSineWave extends Thread {
        private static final String TAG = "GeneratedSineWave";

        public GeneratedSineWave() {
        }

        public void run() {
            controlFreq(step + 1);
        }
    }
}

