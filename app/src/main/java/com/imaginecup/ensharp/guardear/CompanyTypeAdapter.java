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

import com.squareup.picasso.Picasso;

/**
 * Created by MinKyeong on 2017. 2. 26..
 */

public class CompanyTypeAdapter extends ArrayAdapter<Company>{

    Context mContextCompany;
    int mLayoutResourceId;
    String url;

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

        url = currentItem.getUrl().toString();

            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/"+url)
                    .placeholder(R.drawable.apple)
                    .into(imageView);

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


 /*if (currentItem.getID().toString().equals("APPLE")) {
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/"+url)
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        } else if (currentItem.getID().toString().equals("AUDIOTEXHNICA")) {
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/"+ url)
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        } else if(currentItem.getID().toString().equals("CRESYN")){
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/"+url)
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        }else if (currentItem.getID().toString().equals("LG")) {
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/"+url)
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        }else if (currentItem.getID().toString().equals("SAMSUNG")) {
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/o4CVkD2.png")
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        }else if (currentItem.getID().toString().equals("SENNHEISER")) {
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/nklSu9U.png")
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        }else if (currentItem.getID().toString().equals("SHURE")) {
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/lFFfW38.png")
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        }else if (currentItem.getID().toString().equals("SONY")) {
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/M5ud3it.png")
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        }else if (currentItem.getID().toString().equals("WESTONE")) {
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/dZz5jlc.png")
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        }else if (currentItem.getID().toString().equals("XENICS")) {
            Picasso.with(mContextCompany)
                    .load("http://i.imgur.com/LDlQKLU.png")
                    .placeholder(R.drawable.eo_bg920bbkg)
                    .into(imageView);
        }*/
