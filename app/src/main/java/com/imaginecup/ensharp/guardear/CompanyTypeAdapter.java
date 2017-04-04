package com.imaginecup.ensharp.guardear;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MinKyeong on 2017. 2. 26..
 */

public class CompanyTypeAdapter extends ArrayAdapter<Company>{

    Context mContextCompany;
    int mLayoutResourceId;


    public CompanyTypeAdapter(Context context, int layoutResourceId){
        super(context, layoutResourceId);

        mContextCompany = context;
        mLayoutResourceId = layoutResourceId;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView; // 리스트의 각각 한
        final Company currentItem = getItem(position);


        if (row == null) {
            LayoutInflater inflater = ((Activity) mContextCompany).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }
        row.setTag(currentItem);

        final TextView company = (TextView)row.findViewById(R.id.company);
        final ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
        final ImageButton imageButton = (ImageButton)row.findViewById(R.id.imagebutton) ;

        company.setText(currentItem.getID());
        Log.d("이어폰 회사명 확인: ", currentItem.getID().toString());


        if (currentItem.getID().toString().equals("APPLE")) {
            imageView.setImageResource(R.drawable.apple);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        } else if (currentItem.getID().toString().equals("AUDIO-TEXHNICA")) {
            imageView.setImageResource(R.drawable.audiotexhnica);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        } else if(currentItem.getID().toString().equals("CRESYN")){
            imageView.setImageResource(R.drawable.cresyn);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        }else if (currentItem.getID().toString().equals("LG")) {
            imageView.setImageResource(R.drawable.lg);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        }else if (currentItem.getID().toString().equals("SAMSUNG")) {
            imageView.setImageResource(R.drawable.samsung);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        }else if (currentItem.getID().toString().equals("SENNHEISER")) {
            imageView.setImageResource(R.drawable.sennheiser);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        }else if (currentItem.getID().toString().equals("SHURE")) {
            imageView.setImageResource(R.drawable.shure);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        }else if (currentItem.getID().toString().equals("SONY")) {
            imageView.setImageResource(R.drawable.sony);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        }else if (currentItem.getID().toString().equals("WESTONE")) {
            imageView.setImageResource(R.drawable.westone);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        }else if (currentItem.getID().toString().equals("XENICS")) {
            imageView.setImageResource(R.drawable.xenics);
            Log.d("이어폰 회사명 확인 IF문: ", currentItem.getID().toString());
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0){
                Log.d("체크박스 확인 Company : ", " OnClcick " );

                if (imageButton.isPressed()== true) {
                    Log.d("체크박스 확인 Company : ", " if 문");

                    CompanyTypeActivity activity = (CompanyTypeActivity) mContextCompany;

                    activity.checkItem(currentItem);

                }
            }
        });


        return row;
    }


}