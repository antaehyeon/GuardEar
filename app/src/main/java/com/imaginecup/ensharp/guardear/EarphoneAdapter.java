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

        name.setText(currentItem.getID());
        company.setText(currentItem.getCompany());
        checkBox.setChecked(false);
        checkBox.setEnabled(true);
        Log.d("체크박스 확인 : ", " setOnclickListener 전");


            if (currentItem.getID().toString().equals("HSS-100")) {
                imageView.setImageResource(R.drawable.hss_100);
            } else if (currentItem.getID().toString().equals("STORMX_BLITZ")) {
                imageView.setImageResource(R.drawable.stormaxblitz);
            } else if(currentItem.getID().toString().equals("FIX_XE-501")){
                imageView.setImageResource(R.drawable.fixxe_501);
            }else if (currentItem.getID().toString().equals("MDR-EX650AP")) {
                imageView.setImageResource(R.drawable.mdr_ex650ap);
            }else if (currentItem.getID().toString().equals("LG_GS200")) {
                imageView.setImageResource(R.drawable.lg_gs200);
            }else if (currentItem.getID().toString().equals("EO-IG930BBEGKR")) {
                imageView.setImageResource(R.drawable.mo_ig930bbegkr);
            }else if (currentItem.getID().toString().equals("AirPods")) {
                imageView.setImageResource(R.drawable.airpods);
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