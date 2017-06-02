package com.pl.dell.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DELL on 27-05-2017.
 */
public class MySpinAdapter extends ArrayAdapter<logo> {
    private ArrayList<logo> dataSet;

    Context mContext;







    public MySpinAdapter(ArrayList<logo> logos, Context context) {
        super(context,R.layout.list_item,logos);

        this.dataSet=logos;

        this.mContext=context;

    }

        public static class MySpinHolder {
            TextView list;
            ImageView iv;

        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            logo l=dataSet.get(position);
            MySpinHolder viewHolder;

            final View result;

                viewHolder=new MySpinHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                viewHolder.list=(TextView)convertView.findViewById(R.id.list);
                viewHolder.iv=(ImageView)convertView.findViewById(R.id.logo);

                result=convertView;
                convertView.setTag(viewHolder);



                    viewHolder = (MySpinHolder) convertView.getTag();



            viewHolder.list.setText(l.getName());
            viewHolder.iv.setImageDrawable((l.getLogo()));
        return convertView;
        }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){
        return getView(position,convertView,parent);

    }


}
