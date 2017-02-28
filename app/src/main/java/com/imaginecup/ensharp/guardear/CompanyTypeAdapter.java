package com.imaginecup.ensharp.guardear;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MinKyeong on 2017. 2. 26..
 */

public class CompanyTypeAdapter extends ArrayAdapter<CompanyType>{

    Context mContextCompany;

    int mLayoutResourceId;

    public CompanyTypeAdapter(Context context, int layoutResourceId){
        super(context, layoutResourceId);

        mContextCompany = context;
        mLayoutResourceId = layoutResourceId;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final CompanyType currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContextCompany).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }


        row.setTag(currentItem);

        //final Button btn_Next = (Button)row.findViewById(R.id.btn_Next);
        final TextView company = (TextView)row.findViewById(R.id.company);
        final ImageView imageView = (ImageView)row.findViewById(R.id.imageView);

        company.setText(currentItem.getID());
        Log.d("체크박스 확인 : ", " setOnclickListener 전");


        if (currentItem.getID().toString().equals("APPLE")) {
            imageView.setImageResource(R.drawable.apple);
        } else if (currentItem.getID().toString().equals("AUDIO-TEXHNICA")) {
            imageView.setImageResource(R.drawable.audiotexhnica);
        } else if(currentItem.getID().toString().equals("CRESYN")){
            imageView.setImageResource(R.drawable.cresyn);
        }else if (currentItem.getID().toString().equals("LG")) {
            imageView.setImageResource(R.drawable.lg);
        }else if (currentItem.getID().toString().equals("SAMSUNG")) {
            imageView.setImageResource(R.drawable.samsung);
        }else if (currentItem.getID().toString().equals("SENNHEISER")) {
            imageView.setImageResource(R.drawable.sennheiser);
        }else if (currentItem.getID().toString().equals("SHURE")) {
            imageView.setImageResource(R.drawable.shure);
        }else if (currentItem.getID().toString().equals("SONY")) {
            imageView.setImageResource(R.drawable.sony);
        }else if (currentItem.getID().toString().equals("WESTONE")) {
            imageView.setImageResource(R.drawable.westone);
        }else if (currentItem.getID().toString().equals("XENICS")) {
            imageView.setImageResource(R.drawable.xenics);
        }




        Log.d("회사명 체크박스 확인 : ", " 들어오는지 확인");




        //CompanyTypeActivity activity = (CompanyTypeActivity) mContextCompany;

        //activity.getCompany(currentItem);
        //activity.checkItem(currentItem);
        //activity.NextClick(btn_Next);
        //Log.d("체크박스 확인 : ", " " + checkBox.isChecked());

        return row;
    }




}