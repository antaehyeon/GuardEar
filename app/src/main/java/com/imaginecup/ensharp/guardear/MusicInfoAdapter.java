package com.imaginecup.ensharp.guardear;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by MinKyeong on 2017. 4. 27..
 */

public class MusicInfoAdapter extends ArrayAdapter<MusicInfo>{

    Context mContext;
    int mLayoutResourceId;

    public MusicInfoAdapter(Context context,int layoutResourceId){
        super(context,layoutResourceId);

        mContext=context;
        mLayoutResourceId=layoutResourceId;
    }

/**
 * Returns the view for a specific item on the list
 */
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        View row=convertView;

        final MusicInfo currentItem=getItem(position);

        if(row==null){
        LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
        row=inflater.inflate(mLayoutResourceId,parent,false);
        }

        row.setTag(currentItem);

        ToDoActivity activity=(ToDoActivity)mContext;



        return row;
        }

}
