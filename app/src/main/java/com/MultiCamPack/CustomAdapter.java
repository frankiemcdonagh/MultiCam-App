package com.MultiCamPack;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*
    CustomAdapter, created by Frankie McDonagh
    Date:

    This Activity contains code from from:
    Miša Peić, accessed from https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore.
 */

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
        final String path = getItem(position).getName();
        final String startTime = getItem(position).getStartTime();

        //fill in the object with the information
        final VideoListItemModel videoListItemModel = new VideoListItemModel(path, startTime);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvName = convertView.findViewById(R.id.name);
        TextView tvTime = convertView.findViewById(R.id.time);

        //Image
        final ImageView iv  = convertView.findViewById(R.id.addVideo_video_item);

        Bitmap bmThumbnail;

        String imagePath = getRealPathFromUri(mContext, Uri.parse(path));
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MINI_KIND);
        iv.setImageBitmap(bmThumbnail);

        int screenPos = position + 1;
        String name = "Screen " + screenPos;
        tvName.setText(name);
        tvTime.setText(startTime);

        Button buttonAddTime = convertView.findViewById(R.id.item_addTime);
        Button buttonDelete = convertView.findViewById(R.id.item_delete);

        buttonAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, SetStartTimeActivity.class);
                i.putExtra("MainVideo", path);
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
    //See above re: Miša Peić
    private String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally{
            if(cursor!=null)
            {
                cursor.close();
            }
        }

    }
}