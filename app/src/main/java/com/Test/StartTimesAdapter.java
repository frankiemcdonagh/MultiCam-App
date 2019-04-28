package com.Test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import javax.xml.transform.Templates;

public class StartTimesAdapter extends ArrayAdapter<StartTimeModel> {
    public ArrayList<StartTimeModel> list;
    private Context mContext;
    int mResource;
    public StartTimesAdapter(Context context, int resource, ArrayList<StartTimeModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        list = objects;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        String name = Integer.toString(getItem(position).getStartTime());
        TextView tv = convertView.findViewById(R.id.startTimeAv);
        tv.setText(name);

        Button buttonDelete = convertView.findViewById(R.id.item_deleteStartTime);

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
