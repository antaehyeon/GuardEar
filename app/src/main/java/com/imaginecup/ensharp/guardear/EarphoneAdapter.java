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

import com.squareup.picasso.Picasso;

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
    String url;

    public EarphoneAdapter(Context contextear, int layoutResourceId) {

        super(contextear, layoutResourceId);


        Log.d("순서확인중", "EarphoneAdapter(Context contextear, int layoutResourceId)");
        mContextEar = contextear;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("순서확인중", " View getView");
        View row = convertView;

        final Earphone currentItem = getItem(position);

        Log.d("순서확인중", " Earphone currentItem = getItem(position);");


        if (row == null) {
            LayoutInflater inflater = ((Activity) mContextEar).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }


        row.setTag(currentItem);
        final CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkToDoItem);
        final Button btn_Next = (Button)row.findViewById(R.id.btn_Next);
        final TextView name = (TextView)row.findViewById(R.id.name);
        final TextView company = (TextView)row.findViewById(R.id.company); // 모델명
        final ImageView imageView = (ImageView)row.findViewById(R.id.imageView);
        //String imageview;

        name.setText(currentItem.getID());
        url = currentItem.getImage().toString();

        company.setText(currentItem.getModelName());
        checkBox.setChecked(false);
        checkBox.setEnabled(true);
        Log.d("체크박스 확인 : ", " setOnclickListener 전");

        Log.d("체크박스 확인 : ", currentItem.toString());


        Picasso.with(mContextEar)
                .load("http://i.imgur.com/"+url)
                .placeholder(R.drawable.eo_bg920bbkg)
                .into(imageView);


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0){
                Log.d("체크박스 확인 : ", " OnClcick " );


                if (checkBox.isChecked()== true) {
                    Log.d("체크박스 확인 : ", " if 문");

                    EarphoneActivity activity = (EarphoneActivity) mContextEar;

                    activity.checkItem(currentItem, url);

                    activity.NextClick(btn_Next);
                    //Log.d("체크박스 확인 : ", " " + checkBox.isChecked());
                }
            }
        });

        return row;
    }
}



