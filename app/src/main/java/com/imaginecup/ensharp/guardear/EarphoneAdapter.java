package com.imaginecup.ensharp.guardear;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MinKyeong on 2017. 2. 18..
 */

public class EarphoneAdapter extends ArrayAdapter<Earphone> {

    /**
     * Adapter context
     */
    Context mContextEar;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public EarphoneAdapter(Context contextear, int layoutResourceId) {
        super(contextear, layoutResourceId);

        mContextEar = contextear;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final Earphone currentItem = getItem(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContextEar).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }


        row.setTag(currentItem);
        final CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkToDoItem);
        final Button btn_Next = (Button)row.findViewById(R.id.btn_Next);
        final TextView name = (TextView)row.findViewById(R.id.name);
        final TextView company = (TextView)row.findViewById(R.id.company);
        final ImageView imageView = (ImageView)row.findViewById(R.id.imageView);
        //String imageview;

        name.setText(currentItem.getID());
        company.setText(currentItem.getModelName());
        checkBox.setChecked(false);
        checkBox.setEnabled(true);
        Log.d("체크박스 확인 : ", " setOnclickListener 전");


        if (currentItem.getModelName().toString().equals("EO-BG920BBKG")) {
            imageView.setImageResource(R.drawable.eo_bg920bbkg);
        } else if (currentItem.getModelName().toString().equals("EO-EG920BWEG")) {
            imageView.setImageResource(R.drawable.eo_eg920bweg);
        }else if (currentItem.getModelName().toString().equals("EO-BN920CFKG")) {
            imageView.setImageResource(R.drawable.eo_bn920cfkg);
        } else if (currentItem.getModelName().toString().equals("EO-BG930CBKGKR")) {
            imageView.setImageResource(R.drawable.eo_bg930bkgkr);
        }else if (currentItem.getModelName().toString().equals("EO-BG935CBKGKR")) {
            imageView.setImageResource(R.drawable.eo_bg935cbkgkr);
        }else if(currentItem.getModelName().toString().equals("EO-HS1393WEG")) {
            imageView.setImageResource(R.drawable.eo_hs1303weg);
        }else if (currentItem.getModelName().toString().equals("EO-IA510BLKGKR")) {
            imageView.setImageResource(R.drawable.eo_ia510blkgkr);
        }else if (currentItem.getModelName().toString().equals("EO-IG930BBEGKR")) {
            imageView.setImageResource(R.drawable.eo_ig930bbegkr);
        }else if (currentItem.getModelName().toString().equals("EO-MN900KWKG")) {
            imageView.setImageResource(R.drawable.eo_mn900kwkg);
        }else if (currentItem.getModelName().toString().equals("EO-MN910VWKG")) {
            imageView.setImageResource(R.drawable.eo_mn910vwkg);
        }else if (currentItem.getModelName().toString().equals("EO-MG920BBKG")) {
            imageView.setImageResource(R.drawable.eo_mg920bbkg);
        }else{
            imageView.setImageResource(R.drawable.eo_bg935cbkgkr);
        }


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0){
                Log.d("체크박스 확인 : ", " OnClcick " );

                if (checkBox.isChecked()== true) {
                    Log.d("체크박스 확인 : ", " if 문");

                    EarphoneActivity activity = (EarphoneActivity) mContextEar;

                    activity.checkItem(currentItem);

                    activity.NextClick(btn_Next);
                    //Log.d("체크박스 확인 : ", " " + checkBox.isChecked());
                }
            }
        });

        return row;
    }
}