package com.Test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<VideoListItemModel> {
    private static final String TAG = "CustomAdapter";
    public ArrayList<VideoListItemModel> list;
    private Context mContext;
    int mResource;

    public CustomAdapter(Context context, int resource, ArrayList<VideoListItemModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        list = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //get all the information for each field
        final String name = getItem(position).getName();
        final String startTime = getItem(position).getStartTime();

        //fill in the object with the information
        final VideoListItemModel videoListItemModel = new VideoListItemModel(name, startTime);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvName = convertView.findViewById(R.id.name);
        TextView tvTime = convertView.findViewById(R.id.time);

        tvName.setText(name);
        tvTime.setText(startTime);

        ImageButton buttonAddTime = convertView.findViewById(R.id.item_addTime);
        ImageButton buttonDelete = convertView.findViewById(R.id.item_delete);

        buttonAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, SetStartTimeActivity.class);
                i.putExtra("MainVideo", name);
                Bundle b = new Bundle();
                b.putParcelableArrayList("list",list);
                i.putExtras(b);
                mContext.startActivity(i);
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}