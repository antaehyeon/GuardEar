package com.imaginecup.ensharp.guardear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by Semin on 2017-02-20.
 */
public class MyInfo extends AppCompatActivity {

    ImageView mEarphoneImage;
    Bitmap mEarphonePicture;
    private SharedPreferences mPref;
    private boolean mIsAutoVolume;
    private boolean mIsBreakNotification;
    private ImageButton mAutoVolumeBtn;
    private ImageButton mBreakNotificationBtn;
    Context mContextEar;

    public int earphone_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.settingstoolbar);
        setSupportActionBar(toolbar);
        TextView myInfoTitleTxt = (TextView) findViewById(R.id.myInfoTitleTxt);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myInfoTitleTxt.setText("내 정보");
        mPref = new SharedPreferences(this);
        mContextEar = this;

        settingMyInfo();
        settingButton();
    }

    public void settingMyInfo() {
        TextView nameTxt = (TextView) findViewById(R.id.nameTxt);
        TextView ageTxt = (TextView) findViewById(R.id.ageTxt);
        TextView earphone_companyTxt = (TextView) findViewById(R.id.earphone_companyTxt);
        TextView earphone_modelTxt = (TextView) findViewById(R.id.earphone_modelTxt);
        TextView earphone_impedanceTxt = (TextView) findViewById(R.id.earphone_impedanceTxt);
        TextView earphone_soundpressureTxt = (TextView) findViewById(R.id.earphone_soundpressureTxt);
        Button maleBtn = (Button) findViewById(R.id.manBtn);
        Button femaleBtn = (Button) findViewById(R.id.womanBtn);
        Button chageBtn = (Button)findViewById(R.id.btn_change); // 이어폰 수정 버튼
        mEarphoneImage = (ImageView) findViewById(R.id.earphoneImg);

        nameTxt.setText(mPref.getValue("name", "성민경", "userinfo"));
        ageTxt.setText(mPref.getValue("age", "23", "userinfo"));
        earphone_companyTxt.setText(mPref.getValue("earphone_company", "SAMSUNG", "userinfo"));
        earphone_modelTxt.setText(mPref.getValue("earphone_model", "EO-BG920BBKG", "userinfo"));
        earphone_soundpressureTxt.setText(mPref.getValue("earphone_soundpressure", "98", "userinfo"));
        earphone_impedanceTxt.setText(mPref.getValue("earphone_impedance", "16", "userinfo"));

        if (mPref.getValue("sex", "male", "userinfo").equals("male")) {
            maleBtn.setBackgroundResource(R.drawable.chosen_button);
            femaleBtn.setBackgroundResource(R.drawable.not_chosen_button);
        } else {
            maleBtn.setBackgroundResource(R.drawable.not_chosen_button);
            femaleBtn.setBackgroundResource(R.drawable.chosen_button);
        }

        // 서버에서 바로 이미지 정보 받아오기
        String url = mPref.getValue("earphone_image", "", "userinfo");
        Log.d("이어폰 URL", url);

        Picasso.with(this)
                .load("http://i.imgur.com/"+url)
                .placeholder(R.drawable.eo_ig930bbegkr)
                .into(mEarphoneImage);



      /*  mEarphoneImage = (ImageView) findViewById(R.id.earphoneImg);
        mEarphonePicture = resizeImage(mPref.getValue("earphone_image", "eo_bg920bbkg", "userinfo"), 190, 190);
        mEarphoneImage.setImageBitmap(getCircleBitmap(mEarphonePicture));*/


        chageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                earphone_count++;

                Intent intent = new Intent(getApplicationContext(), CompanyTypeActivity.class);

                intent.putExtra("count", 1);

                startActivity(intent);


            }
        });


    }

    public Bitmap resizeImage(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public void analysis_click(View view) {
        Intent intent = new Intent(getBaseContext(), WeeklyInfoActivity.class);
        startActivity(intent);
    }

    private void settingButton() {
        mAutoVolumeBtn = (ImageButton) findViewById(R.id.autoVolumeBtn);
        mBreakNotificationBtn = (ImageButton) findViewById(R.id.breakNotificationBtn);
        mIsAutoVolume = mPref.getValue("autovolume", false, "setting");
        mIsBreakNotification = mPref.getValue("breaknotification", false, "setting");

        if (mIsAutoVolume) {
            mAutoVolumeBtn.setImageResource(R.drawable.onswitch);
        } else {
            mAutoVolumeBtn.setImageResource(R.drawable.offswitch);
        }

        if (mIsBreakNotification) {
            mBreakNotificationBtn.setImageResource(R.drawable.onswitch);
        } else {
            mBreakNotificationBtn.setImageResource(R.drawable.offswitch);
        }

        mAutoVolumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsAutoVolume) {
                    mAutoVolumeBtn.setImageResource(R.drawable.offswitch);
                    mPref.putValue("autovolume", false, "setting");
                    mIsAutoVolume = false;
                } else {
                    mAutoVolumeBtn.setImageResource(R.drawable.onswitch);
                    mPref.putValue("autovolume", true, "setting");
                    mIsAutoVolume = true;
                }
            }
        });

        mBreakNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBreakNotification) {
                    mBreakNotificationBtn.setImageResource(R.drawable.offswitch);
                    mPref.putValue("breaknotification", false, "setting");
                    mIsBreakNotification = false;
                } else {
                    mBreakNotificationBtn.setImageResource(R.drawable.onswitch);
                    mPref.putValue("breaknotification", true, "setting");
                    mIsBreakNotification = true;
                    Intent intent = new Intent(MyInfo.this, BreakNotifyService.class);
                    startService(intent);
                }
            }
        });
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
